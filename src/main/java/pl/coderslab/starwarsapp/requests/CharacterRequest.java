package pl.coderslab.starwarsapp.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CharacterRequest {
    @NotNull
    private Long id;
}
