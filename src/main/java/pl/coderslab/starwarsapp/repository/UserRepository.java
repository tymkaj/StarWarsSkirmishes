package pl.coderslab.starwarsapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.starwarsapp.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);
}
