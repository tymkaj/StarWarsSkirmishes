package pl.coderslab.starwarsapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BattleDTO {
    private Long id;
    private UserDTO winner;
    private UserDTO loser;
    private TeamDTO teamA;
    private TeamDTO teamB;
    private TeamDTO winnerTeam;
}
