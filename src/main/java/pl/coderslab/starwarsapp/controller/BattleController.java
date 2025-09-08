package pl.coderslab.starwarsapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.starwarsapp.dto.BattleDTO;
import pl.coderslab.starwarsapp.dto.PageDTO;
import pl.coderslab.starwarsapp.filter.BattleFilter;
import pl.coderslab.starwarsapp.requests.BattleRequest;
import pl.coderslab.starwarsapp.service.BattleService;

@Tag(
        name = "Battles",
        description = "Operations including battles"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/swApi/battle")
public class BattleController {

    private final BattleService battleService;

    @Operation(
            summary = "Get all battles",
            description = "Returns battles according to passed params"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Battles returned successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request, make sure params are correct"
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
    public ResponseEntity<PageDTO<BattleDTO>> getBattles(HttpServletRequest request,
                                                         @RequestParam(defaultValue = "0") @Min(0) int page,
                                                         @RequestParam(defaultValue = "10") @Min(0) @Max(10) int size,
                                                         @RequestParam(defaultValue = "ALL") BattleFilter filter,
                                                         @RequestParam(defaultValue = "DESC") Sort.Direction dir) {
        return battleService.getAllBattles(request, page, size, filter, dir);
    }

    @Operation(
            summary = "Commence battle",
            description = "Commences battle between two users passed through BattleRequest"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Battle commenced successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request, make sure params are correct"
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
    @PostMapping("/commence")
    public ResponseEntity<BattleDTO> commenceBattle(@Valid @RequestBody BattleRequest request) {
        return battleService.getBattleResult(request);
    }

}
