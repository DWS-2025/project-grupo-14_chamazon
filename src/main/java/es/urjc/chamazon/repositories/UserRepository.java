package es.urjc.chamazon.repositories;

import es.urjc.chamazon.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
