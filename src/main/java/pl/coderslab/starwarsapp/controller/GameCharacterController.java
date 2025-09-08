package pl.coderslab.starwarsapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.starwarsapp.dto.GameCharacterDTO;
import pl.coderslab.starwarsapp.dto.PageDTO;
import pl.coderslab.starwarsapp.requests.CharacterRequest;
import pl.coderslab.starwarsapp.service.GameCharacterService;

import java.util.List;

@Tag(
        name = "Characters",
        description = "Operations including characters"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/swApi/characters")
public class GameCharacterController {

    private final GameCharacterService gameCharacterService;

    @Operation(
            summary = "Get all characters",
            description = "Returns a GameCharacterDTO list featuring all of the characters"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List returned successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized access or session expired"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access forbidden, log in first"
            )
    })
    @GetMapping
    public ResponseEntity<List<GameCharacterDTO>> getAllCharacters() {
        return gameCharacterService.findAll();
    }

    @Operation(
            summary = "Get a specific character's details by its ID",
            description = "Returns a GameCharacterDTO object of a specific character"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Character returned successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request. Make sure you pass the correct ID"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized access or session expired"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access forbidden, log in first"
            )
    })
    @PostMapping("/details")
    public ResponseEntity<GameCharacterDTO> getCharacter(@RequestBody CharacterRequest request) {
        return gameCharacterService.getCharacterById(request);
    }

    @Operation(
            summary = "Return characters matching the query string",
            description = "Returns a PageDTO GameCharacterDTO object of a specific character"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Character returned successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request. Make sure you pass the correct params"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized access or session expired"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access forbidden, log in first"
            )
    })
    @GetMapping("/search")
    public ResponseEntity<PageDTO<GameCharacterDTO>> searchCharacters(@RequestParam String q,
                                                                      @RequestParam(defaultValue = "0") @Min(0) int page,
                                                                      @RequestParam(defaultValue = "10") @Min(0) @Max(10) int size) {
        return gameCharacterService.searchCharacters(q, page, size);
    }

}
