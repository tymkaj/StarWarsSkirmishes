package pl.coderslab.starwarsapp.mapper;

import org.mapstruct.Mapper;
import pl.coderslab.starwarsapp.dto.BattleDTO;
import pl.coderslab.starwarsapp.entity.Battle;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, TeamMapper.class})
public interface BattleMapper {

    BattleDTO battleToBattleDTO(Battle battle);
    Battle battleDTOToBattle(BattleDTO battleDTO);
    List<BattleDTO> battleListToBattleDTOList(List<Battle> battleList);
    List<Battle> battleDTOListToBattleList(List<BattleDTO> battleDTOList);

}
