package pl.coderslab.starwarsapp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Battle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "winner_id")
    private User winner;
    @ManyToOne
    @JoinColumn(name = "loser_id")
    private User loser;
    @ManyToOne
    @JoinColumn(name = "team_a_id")
    private Team teamA;
    @ManyToOne
    @JoinColumn(name = "team_b_id")
    private Team teamB;
    @ManyToOne
    @JoinColumn(name = "winning_team_id")
    private Team winnerTeam;
}
