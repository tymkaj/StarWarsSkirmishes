package pl.coderslab.starwarsapp.mapper;

import org.mapstruct.Mapper;
import pl.coderslab.starwarsapp.dto.GameCharacterDTO;
import pl.coderslab.starwarsapp.entity.GameCharacter;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GameCharacterMapper {

    GameCharacterDTO gameCharacterToGameCharacterDTO(GameCharacter gameCharacter);
    List<GameCharacterDTO> gameCharacterListToGameCharacterDTOList(List<GameCharacter> gameCharacterList);

}
