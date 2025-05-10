package es.urjc.chamazon.configurations;


import es.urjc.chamazon.services.RepositoryUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    public RepositoryUserDetailsService userDetailService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        http
                .authorizeHttpRequests(authorize -> authorize
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


                        .requestMatchers("/commentView/edit/{id}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/commentView/delete/{id}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/commentView/add").hasAnyRole("ADMIN", "USER")

                        .requestMatchers("/users/edit").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/user").hasRole("USER")
                        .requestMatchers("user/delete").hasAnyRole("ADMIN", "USER")

                        .requestMatchers("/categories/add").hasRole("ADMIN")
                        .requestMatchers("/categories/delete").hasRole("ADMIN")
                        .requestMatchers("/categories/edit").hasRole("ADMIN")
                        .requestMatchers("/products/add").hasRole("ADMIN")
                        .requestMatchers("/products/{id}/edit").hasRole("ADMIN")
                        .requestMatchers("/products/{id}/delete").hasRole("ADMIN")
                        .requestMatchers("/users").hasRole("ADMIN")


                        .requestMatchers("/shoppingCar/history/{idUser}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/shoppingCar/{id}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/shoppingCar/endPurchase/{idUser}").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/shoppingCar/removeProduct/{idProduct}/{idUser}").hasAnyRole("USER", "ADMIN")

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
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }
}
