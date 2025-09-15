package pl.coderslab.starwarsapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.starwarsapp.entity.GameCharacter;

import java.util.Optional;

public interface GameCharacterRepository extends JpaRepository<GameCharacter, Long> {
    Page<GameCharacter> findByNameStartingWithIgnoreCase(String name, Pageable pageable);
    Optional<GameCharacter> findByName(String name);
}
