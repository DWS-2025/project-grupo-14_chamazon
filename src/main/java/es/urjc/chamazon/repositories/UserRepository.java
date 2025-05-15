package es.urjc.chamazon.repositories;

import es.urjc.chamazon.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);

    //Method to check if a user already exists by username. We will encapsulate it in the UserService
    // for using it in the controller before creating a new user to check
    // if the username is already taken.
    boolean existsByUserName(String username);
}

