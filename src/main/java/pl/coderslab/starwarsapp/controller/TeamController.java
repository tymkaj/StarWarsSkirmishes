package pl.coderslab.starwarsapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.starwarsapp.dto.TeamDTO;
import pl.coderslab.starwarsapp.dto.UserDTO;
import pl.coderslab.starwarsapp.requests.TeamCreateRequest;
import pl.coderslab.starwarsapp.requests.TeamDeleteRequest;
import pl.coderslab.starwarsapp.service.TeamService;

import java.util.List;
import java.util.Map;

@Tag(
        name = "Teams",
        description = "Operations including teams"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/swApi/teams")
public class TeamController {

    private final TeamService teamService;

    @Operation(
            summary = "Get a list of all the teams of the logged in user",
            description = "Returns a list of TeamDTO objects, featuring IDs, names and characters included in all of the teams of the logged in user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of TeamDTO returned successfully"
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
    public ResponseEntity<List<TeamDTO>> getTeams(HttpServletRequest request) {
        return teamService.getTeams(request);
    }

    @Operation(
            summary = "Create a team",
            description = "Upon a successful request, create a team"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Team created successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized access or session expired"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request, teamCreateRequest requires a name and a list of characters"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access forbidden, log in first"
            )
    })
    @PostMapping("/create")
    public ResponseEntity<TeamDTO> createTeam(@Valid @RequestBody TeamCreateRequest teamCreateRequest, HttpServletRequest request) {
        return teamService.createTeam(teamCreateRequest, request);
    }

    @Operation(
            summary = "Return user's teams",
            description = "Upon a successful request, return a specific user's teams"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Teams returned successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized access or session expired"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request, userDTO requires an id and a username of an existing user"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access forbidden, log in first"
            )
    })
    @PostMapping("/userTeams")
    public ResponseEntity<List<TeamDTO>> userTeams(@RequestBody UserDTO userDTO) {
        return teamService.getUserTeams(userDTO);
    }

    @Operation(
            summary = "Soft delete a team",
            description = "Upon a successful request, change a team's 'deleted' attribute to 'true', making it unusable by the user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Team soft deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized access or session expired"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request, teamDeleteRequest requires ID and name of the team to be soft deleted"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access forbidden, log in first"
            )
    })
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteTeam(@Valid @RequestBody TeamDeleteRequest teamDeleteRequest, HttpServletRequest request) {
        return teamService.deleteTeam(teamDeleteRequest, request);
    }

}
