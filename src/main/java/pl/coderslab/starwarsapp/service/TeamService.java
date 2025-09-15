package pl.coderslab.starwarsapp.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import pl.coderslab.starwarsapp.dto.TeamDTO;
import pl.coderslab.starwarsapp.dto.UserDTO;
import pl.coderslab.starwarsapp.entity.GameCharacter;
import pl.coderslab.starwarsapp.entity.Team;
import pl.coderslab.starwarsapp.entity.User;
import pl.coderslab.starwarsapp.mapper.TeamMapper;
import pl.coderslab.starwarsapp.repository.BattleRepository;
import pl.coderslab.starwarsapp.repository.GameCharacterRepository;
import pl.coderslab.starwarsapp.repository.TeamRepository;
import pl.coderslab.starwarsapp.repository.UserRepository;
import pl.coderslab.starwarsapp.requests.TeamCreateRequest;
import pl.coderslab.starwarsapp.requests.TeamDeleteRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final UserRepository userRepository;
    private final GameCharacterRepository gameCharacterRepository;

    @Transactional
    public ResponseEntity<List<TeamDTO>> getTeams(HttpServletRequest request) {
        HttpSession session = request.getSession();
        List<Team> teams = teamRepository.findAllByUserUsernameAndDeletedFalse(session.getAttribute("username").toString());
        List<TeamDTO> teamsDTO = teamMapper.teamsToTeamDTO(teams);
        return ResponseEntity.ok(teamsDTO);
    }

    @Transactional
    public ResponseEntity<List<TeamDTO>> getUserTeams(UserDTO userDTO) {
        List<Team> teams = teamRepository.findAllByUserUsernameAndDeletedFalse(userDTO.getUsername());
        List<TeamDTO> teamsDTO = teamMapper.teamsToTeamDTO(teams);
        return ResponseEntity.ok(teamsDTO);
    }

    public ResponseEntity<TeamDTO> createTeam(TeamCreateRequest teamRequest, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Optional<User> maybeUser = userRepository.findByUsername(session.getAttribute("username").toString());
        User user = maybeUser.orElseThrow(() -> new EntityNotFoundException("User not found"));
        Team team = new Team();
        List<GameCharacter> characters = teamRequest.getCharacters().stream()
                        .map(id -> gameCharacterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Character not found")))
                        .toList();
        team.setName(teamRequest.getName());
        team.setCharacters(characters);
        team.setUser(user);
        teamRepository.save(team);
        return ResponseEntity.status(HttpStatus.CREATED).body(teamMapper.teamToTeamDTO(team));
    }

    @Transactional
    public ResponseEntity<Map<String, String>> deleteTeam(TeamDeleteRequest teamDeleteRequest, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long teamId = teamDeleteRequest.getId();
        String teamName = teamDeleteRequest.getName();
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found" + teamId));

        if (!team.getUser().getUsername().equals(session.getAttribute("username").toString())) {
            throw new AccessDeniedException("You are not the owner of this team");
        }

        team.setDeleted(true);
        teamRepository.save(team);

        return ResponseEntity.ok(Map.of("message", "Team " + teamName + " has been deleted successfully"));
    }

}
