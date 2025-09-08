package pl.coderslab.starwarsapp.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.coderslab.starwarsapp.dto.GameCharacterDTO;
import pl.coderslab.starwarsapp.dto.PageDTO;
import pl.coderslab.starwarsapp.entity.GameCharacter;
import pl.coderslab.starwarsapp.mapper.GameCharacterMapper;
import pl.coderslab.starwarsapp.repository.GameCharacterRepository;
import pl.coderslab.starwarsapp.requests.CharacterRequest;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameCharacterService {

    private final GameCharacterRepository gameCharacterRepository;
    private final GameCharacterMapper gameCharacterMapper;

    public ResponseEntity<List<GameCharacterDTO>> findAll() {
        return ResponseEntity.ok(gameCharacterMapper.gameCharacterListToGameCharacterDTOList(gameCharacterRepository.findAll()));
    }

    public ResponseEntity<GameCharacterDTO> getCharacterById(CharacterRequest request) {
        Optional<GameCharacter> maybeCharacter = gameCharacterRepository.findById(request.getId());
        return maybeCharacter.map(gameCharacter -> ResponseEntity.ok(gameCharacterMapper.gameCharacterToGameCharacterDTO(gameCharacter))).orElseThrow(() -> new EntityNotFoundException("Character not found"));
    }

    public ResponseEntity<PageDTO<GameCharacterDTO>> searchCharacters(String input, int page, int size) {
        String q = Optional.ofNullable(input).orElse("").trim();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));

        Page<GameCharacter> p = gameCharacterRepository.findByNameStartingWithIgnoreCase(q, pageable);

        List<GameCharacterDTO> items = gameCharacterMapper.gameCharacterListToGameCharacterDTOList(p.getContent());
        PageDTO<GameCharacterDTO> body = new PageDTO<>(items, p.getNumber(), p.getSize(),
                p.getTotalElements(), p.getTotalPages(), p.hasNext(), p.hasPrevious());
        return ResponseEntity.ok(body);
    }

}
