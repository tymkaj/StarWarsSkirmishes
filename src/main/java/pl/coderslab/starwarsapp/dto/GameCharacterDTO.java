package pl.coderslab.starwarsapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GameCharacterDTO {
    private Long id;
    private String name;
    private String role;
    private String affiliation;
    private String powerLvl;
    private String imgUrl;
}
