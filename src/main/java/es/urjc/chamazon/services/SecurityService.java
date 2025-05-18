package es.urjc.chamazon.services;

import es.urjc.chamazon.configurations.SecurityUtils;
import es.urjc.chamazon.models.ShoppingCar;
import es.urjc.chamazon.models.User;
import es.urjc.chamazon.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityService {


    @Autowired
    @Lazy
    UserService userService;
    @Autowired
    private ShoppingCarService shoppingCarService;


/*    @Autowired
    private UserRepository userRepository;
    public User getUserByName(String name) {
        Optional<User> userOpt = userRepository.findByUserName(name);
        return userOpt.orElse(null);
    }*/

    private String sesionUserName() {
        return SecurityUtils.getCurrentUsername();
    }

    private User sesionUser(){
        return userService.getUserByName(sesionUserName());
    }

    private Authentication getAuthentication(){
        return SecurityUtils.getAuthentication();
    }

    boolean isAdmin() {
        return SecurityUtils.isAdmin();
    }

    boolean isAuthenticated(){
        return SecurityUtils.isAuthenticated();
    }

    boolean isAuthorized(Long userID) {

        Authentication auth = this.getAuthentication();

        return auth != null && auth.isAuthenticated() &&
                !(auth instanceof AnonymousAuthenticationToken) &&
                userID.equals(sesionUser().getId());
    }

    public void permission(Long idUser) throws Exception {
        boolean notSecure = false;
        if ( !this.isAuthorized(idUser) && !this.isAdmin() ) {
            throw new Exception("No tiene permisos para acceder a la pagina");
        }
    }

    public void permission() throws Exception {
        boolean notSecure = false;
        if ( !this.isAuthenticated() && !this.isAdmin() ) {
            throw new Exception("No tiene permisos para acceder a la pagina");
        }
    }


    public void shoppingCarPermission(Long shoppingCarID) throws Exception {
        ShoppingCar sc = shoppingCarService.getShoppingCarById(shoppingCarID);
        this.permission(sc.getUserId());
    }
}
