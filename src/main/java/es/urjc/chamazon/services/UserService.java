
package es.urjc.chamazon.services;

import es.urjc.chamazon.configurations.SecurityConfiguration;
import es.urjc.chamazon.configurations.SecurityUtils;
import es.urjc.chamazon.dto.UserDTO;
import es.urjc.chamazon.dto.UserDTOExtended;
import es.urjc.chamazon.dto.UserMapper;
import es.urjc.chamazon.models.Comment;
import es.urjc.chamazon.models.User;
import es.urjc.chamazon.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ShoppingCarService shoppingCarService;

    @Autowired
    private SanitizationService sanitizationService;

    @Autowired
    private RepositoryUserDetailsService userDetailService;

    public List<UserDTO> getAllUsers() {
        return toDTOs(userRepository.findAll());
    }

    public UserDTO getUser(Long Id) {
        return toDTO(userRepository.findById(Id).orElseThrow());
    }

    User getUserById(Long Id) {
        Optional<User> userOpt = userRepository.findById(Id);
        return userOpt.orElse(null);
    }

    public Optional<UserDTOExtended> findByUserName(String username) {
        return userRepository.findByUserName(username)
                .map(userMapper::toDTOExtended);
    }

    public void deleteUser(Long id, HttpServletRequest request) {

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            for (Comment comment : user.getComments()) {
                comment.setUser(null);
            }
            user.getComments().clear();

            userRepository.save(user);

            userRepository.delete(user);
        }
        SecurityUtils.logout(request);

    }

    public void saveNewUser(UserDTO userDTO) {
        UserDTO sanitizedUserDTO = sanitizationService.sanitizeUserDTO(userDTO);
        User user = toUser(sanitizedUserDTO);
        user.setPassword( passwordEncoder.encode(user.getPassword()));
        saveUser(user);
    }

    public void save(UserDTO userDTO) {
        UserDTO sanitizedUserDTO = sanitizationService.sanitizeUserDTO(userDTO);
        User user = toUser(sanitizedUserDTO);
        saveUser(user);

    }
    void saveUser(User user) {
        // To encode the password before saving the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // When a user is created, it is assigned the role of USER by default
        if (user.getType() == null || user.getType().isEmpty()) {
            user.setType(List.of("USER"));
        }
        userRepository.save(user);
        shoppingCarService.assignSCToUser(user);
    }

    public void updateUser(Long id, UserDTO updatedUserDTO) {
        UserDTO sanitizedUpdatedUserDTO = sanitizationService.sanitizeUserDTO(updatedUserDTO);
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();

            String currentUsername = SecurityUtils.getCurrentUsername();
            System.out.println("currentUsername: " + currentUsername);
            boolean isSelfEdit = currentUsername.equals(existingUser.getUserName());
            String newUsername = sanitizedUpdatedUserDTO.userName();

            if (!existingUser.getUserName().equals(newUsername)) {
                if (userRepository.findByUserName(newUsername).isPresent()) {
                    throw new IllegalArgumentException("El nombre de usuario ya está en uso");
                }
                existingUser.setUserName(newUsername);
            }

            // Solo actualizamos los campos desde el DTO, sin crear una nueva instancia User
            existingUser.setFirstName(sanitizedUpdatedUserDTO.firstName());
            existingUser.setSurName(sanitizedUpdatedUserDTO.surName());
            existingUser.setEmail(sanitizedUpdatedUserDTO.email());
            existingUser.setPhone(sanitizedUpdatedUserDTO.phone());
            existingUser.setAddress(sanitizedUpdatedUserDTO.address());

            String newPassword = sanitizedUpdatedUserDTO.password();
            if (newPassword != null && !newPassword.isEmpty()) {
                String encodedPassword = passwordEncoder.encode(newPassword);
                existingUser.setPassword(encodedPassword);
            }

            userRepository.save(existingUser);

            // Si el usuario está editando su propio username, actualizamos el contexto de autenticación
            if (isSelfEdit && !currentUsername.equals(newUsername)) {
                UserDetails updatedUserDetails = userDetailService.loadUserByUsername(newUsername);
                Authentication newAuth = new UsernamePasswordAuthenticationToken(
                        updatedUserDetails,
                        updatedUserDetails.getPassword(),
                        updatedUserDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(newAuth);
            }
        }
    }

    // Logic of userName comprobation encapsulated in the service
    public boolean userNameExists (String userName) {
        return userRepository.existsByUserName(userName);
    }


    private UserDTO toDTO(User user){
        return userMapper.toDTO(user);
    }
    private User toUser(UserDTO userDTO){
        return userMapper.toUser(userDTO);
    }
    private List<UserDTO> toDTOs(List<User> users){
        return userMapper.toDTOs(users);
    }
    private List<User> toUsers(List<UserDTO> usersDTO){
        return userMapper.toUsers(usersDTO);
    }
    private UserDTOExtended toDTOExtended(User user){
        return userMapper.toDTOExtended(user);
    }






    /*
    public void addUser(User user) {
        users.put(user.getId(), user);
        shoppingCarService.addNewSoppingCarToUser(user.getId());
    }


    public void getShoppingFromUser(int id) {
        User user = users.get(id);
        if (user != null) {
            System.out.println("Carrito de " + user.getName() + ": " + shoppingCarService.getActualShoppingCarByIdUser(user.getId()));
        } else {
            System.out.println("Usuario no encontrado.");
        }
    }
    public void addShoppingToUser(int id, Product product) {
        User user = users.get(id);
        if (user != null) {
            //user.getShoppingCart().add(product);
            System.out.println("Producto añadido al carrito de " + user.getName());
        } else {
            System.out.println("Usuario no encontrado.");
        };
    }

    public void updateUser(User existingUser) {
        users.put(existingUser.getId(), existingUser);
    }*/
}

