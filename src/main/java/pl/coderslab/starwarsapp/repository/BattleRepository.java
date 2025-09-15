package pl.coderslab.starwarsapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.coderslab.starwarsapp.entity.Battle;

public interface BattleRepository extends JpaRepository<Battle, Long> {
    @Query("SELECT b FROM Battle b WHERE b.winner.id = :userId OR b.loser.id = :userId")
    Page<Battle> findAllByUserInvolved(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT b FROM Battle b WHERE b.winner.id = :userId")
    Page<Battle> findAllWonByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT b FROM Battle b WHERE b.loser.id = :userId")
    Page<Battle> findAllLostByUserId(@Param("userId") Long userId, Pageable pageable);

}
