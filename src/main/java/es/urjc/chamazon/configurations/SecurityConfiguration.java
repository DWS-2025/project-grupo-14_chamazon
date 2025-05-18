package es.urjc.chamazon.configurations;


import es.urjc.chamazon.services.RepositoryUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

import static org.springframework.security.config.Customizer.withDefaults;

import es.urjc.chamazon.jwt.UnauthorizedHandlerJwt;
import es.urjc.chamazon.jwt.JwtRequestFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    public RepositoryUserDetailsService userDetailService;

    @Autowired
    private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }


    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        http
                .securityMatcher("/api/**")
                .exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt))
                .authorizeHttpRequests(authorize -> authorize

                        // === PUBLIC API (sin login) ===
                        .requestMatchers(HttpMethod.GET,
                                "/api/products",
                                "/api/products/filter",
                                "/api/products/{id}",
                                "/api/products/{id}/images",
                                "/api/categories",
                                "/api/categories/",
                                "/api/categories/{id}",
                                "/api/categories/{id}/products",
                                "/api/comments/",
                                "/api/comments/{id}",
                                "/api/products/{id}/file"
                        ).permitAll()


                        // === COMMENTS (USER o ADMIN) ===
                        .requestMatchers(HttpMethod.POST, "/api/comments/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/comments/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/comments/**").hasAnyRole("USER", "ADMIN")

                        // === CATEGORIES (ADMIN) ===
                        .requestMatchers(HttpMethod.POST, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasRole("ADMIN")

                        // === PRODUCTS (ADMIN) ===
                        .requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")

                        // === SHOPPING CART (USER o ADMIN) ===
                        .requestMatchers("/api/shoppingCar/**").hasAnyRole("USER", "ADMIN")

                        // === USERS ===
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")               // ver todos los usuarios
                        .requestMatchers(HttpMethod.GET, "/api/users/{id}").hasAnyRole("ADMIN","USER")           // ver un usuario específico (filtrado después)
                        .requestMatchers(HttpMethod.POST, "/api/users/**").permitAll()                // registro libre
                        .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasAnyRole("ADMIN","USER")           // puede editar (se filtra después)
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasAnyRole("ADMIN","USER")       // puede borrar (se filtra después)

                        // === Anything else in /api/** (no autorizado por defecto) ===
                        .anyRequest().permitAll()
                );


        // Disable Form login Authentication
        http.formLogin(AbstractHttpConfigurer::disable);

        // Disable CSRF protection (it is difficult to implement in REST APIs)
        http.csrf(csrf -> csrf.disable());

        // Disable Basic Authentication
        http.httpBasic(httpBasic -> httpBasic.disable());

        // Stateless session
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Add JWT Token filter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        /*http.headers(headers -> headers
                .contentSecurityPolicy(csp -> csp
                        .policyDirectives(
                                "default-src 'self'; " +
                                        "script-src 'self' https://cdn.quilljs.com; " +
                                        "style-src 'self' https://cdn.quilljs.com; " +
                                        "img-src 'self' data:; " +
                                        "object-src 'none'; " +
                                        "frame-ancestors 'self'; " +
                                        "form-action 'self'; " +
                                        "base-uri 'none'; " +
                                        "upgrade-insecure-requests;"))
                .httpStrictTransportSecurity(hsts -> hsts
                        .includeSubDomains(true)
                        .maxAgeInSeconds(31536000))
                .frameOptions(frame -> frame.sameOrigin())
                .contentTypeOptions(withDefaults())
                .referrerPolicy(referrer -> referrer
                        .policy(org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER))
                .xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)));
               */
        http
                .authorizeHttpRequests(authorize -> authorize
                        //PUBLIC PAGES
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("login").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/register/**").permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/js/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/products").permitAll()
                        .requestMatchers("/categories").permitAll()
                        .requestMatchers("products/filter").permitAll()
                        .requestMatchers("/products/{id}").permitAll()
                        .requestMatchers("/products/{id}/images").permitAll()
                        .requestMatchers("/categories/products").permitAll()
                        .requestMatchers("/commentView/commentList").permitAll()

                        //REGISTER PAGES
                        .requestMatchers("/commentView/edit/{id}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/commentView/delete/{id}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/commentView/add").hasAnyRole("ADMIN", "USER")

                        .requestMatchers("/users/edit").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/user").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/user/delete").hasAnyRole("ADMIN", "USER")

                        .requestMatchers("/shoppingCar/history/{idUser}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/shoppingCar/{id}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/shoppingCar/endPurchase/{idUser}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/shoppingCar/removeProduct/{idProduct}/{idUser}").hasAnyRole("USER", "ADMIN")

                        //ADMIN PAGES
                        .requestMatchers("/categories/add").hasRole("ADMIN")
                        .requestMatchers("/categories/delete").hasRole("ADMIN")
                        .requestMatchers("/categories/edit").hasRole("ADMIN")
                        .requestMatchers("/products/add").hasRole("ADMIN")
                        .requestMatchers("/products/{id}/edit").hasRole("ADMIN")
                        .requestMatchers("/products/{id}/delete").hasRole("ADMIN")
                        .requestMatchers("/users").hasRole("ADMIN")
                        .requestMatchers("/users/add").hasRole("ADMIN")

                        .anyRequest().authenticated()

                )

                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .failureUrl("/loginerror")
                        .defaultSuccessUrl("/")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        // Disable CSRF at the moment
        //http.csrf(csrf -> csrf.disable());

        return http.build();
    }
}
