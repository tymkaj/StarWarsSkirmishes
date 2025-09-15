package pl.coderslab.starwarsapp.mapper;

import org.mapstruct.Mapper;
import pl.coderslab.starwarsapp.dto.TeamDTO;
import pl.coderslab.starwarsapp.entity.Team;

import java.util.List;

@Mapper(componentModel = "spring", uses = GameCharacterMapper.class)
public interface TeamMapper {

    TeamDTO teamToTeamDTO(Team team);
    List<TeamDTO> teamsToTeamDTO(List<Team> teams);

}
