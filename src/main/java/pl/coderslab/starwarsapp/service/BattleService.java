package pl.coderslab.starwarsapp.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.coderslab.starwarsapp.dto.BattleDTO;
import pl.coderslab.starwarsapp.dto.PageDTO;
import pl.coderslab.starwarsapp.entity.Battle;
import pl.coderslab.starwarsapp.entity.Team;
import pl.coderslab.starwarsapp.entity.User;
import pl.coderslab.starwarsapp.filter.BattleFilter;
import pl.coderslab.starwarsapp.mapper.BattleMapper;
import pl.coderslab.starwarsapp.repository.BattleRepository;
import pl.coderslab.starwarsapp.repository.TeamRepository;
import pl.coderslab.starwarsapp.repository.UserRepository;
import pl.coderslab.starwarsapp.requests.BattleRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BattleService {

    private final BattleRepository battleRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final BattleMapper battleMapper;

    @Transactional
    public ResponseEntity<PageDTO<BattleDTO>> getAllBattles(HttpServletRequest request, int page, int size, BattleFilter filter,
                                                            Sort.Direction dir) {

        Long userId = (Long) request.getSession().getAttribute("userId");

        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, "id"));

        Page<Battle> p = switch (filter) {
            case WON  -> battleRepository.findAllWonByUserId(userId, pageable);
            case LOST -> battleRepository.findAllLostByUserId(userId, pageable);
            default   -> battleRepository.findAllByUserInvolved(userId, pageable);
        };

        List<BattleDTO> items = battleMapper.battleListToBattleDTOList(p.getContent());
        PageDTO<BattleDTO> body = new PageDTO<>(
                items, p.getNumber(), p.getSize(), p.getTotalElements(),
                p.getTotalPages(), p.hasNext(), p.hasPrevious()
        );

        return ResponseEntity.ok(body);
    }

    @Transactional
    public ResponseEntity<BattleDTO> getBattleResult(BattleRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Team userTeam = teamRepository.findById(request.getTeamId()).orElseThrow(() -> new EntityNotFoundException("Team not found"));
        User opponent = userRepository.findById(request.getOpponentId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Team opponentTeam = teamRepository.findById(request.getOpponentTeamId()).orElseThrow(() -> new EntityNotFoundException("Team not found"));
        int userTeamPower = userTeam.getCharacters().stream()
                .mapToInt(c -> Integer.parseInt(c.getPowerLvl()))
                .sum();
        int opponentTeamPower = opponentTeam.getCharacters().stream()
                .mapToInt(c -> Integer.parseInt(c.getPowerLvl()))
                .sum();
        Battle battle = new Battle();
        battle.setTeamA(userTeam);
        battle.setTeamB(opponentTeam);

        if (userTeamPower >= opponentTeamPower) {
            battle.setWinner(user);
            battle.setLoser(opponent);
            battle.setWinnerTeam(userTeam);
        } else {
            battle.setWinner(opponent);
            battle.setLoser(user);
            battle.setWinnerTeam(opponentTeam);
        }

        battleRepository.save(battle);
        return ResponseEntity.ok(battleMapper.battleToBattleDTO(battle));
    }

}
