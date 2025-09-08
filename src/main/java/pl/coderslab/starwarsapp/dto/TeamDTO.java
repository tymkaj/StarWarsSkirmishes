package pl.coderslab.starwarsapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TeamDTO {
    private Long id;
    private String name;
    private List<GameCharacterDTO> characters;
}
