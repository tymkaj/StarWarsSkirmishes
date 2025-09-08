package pl.coderslab.starwarsapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.starwarsapp.entity.Team;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findAllByUserUsernameAndDeletedFalse(String username);
}
