package pl.coderslab.starwarsapp.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BattleRequest {
    @NotNull
    private Long userId;
    @NotNull
    private Long teamId;
    @NotNull
    private Long opponentId;
    @NotNull
    private Long opponentTeamId;
}
