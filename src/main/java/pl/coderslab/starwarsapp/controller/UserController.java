package pl.coderslab.starwarsapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.coderslab.starwarsapp.dto.UserDTO;
import pl.coderslab.starwarsapp.service.UserService;

import java.util.List;

@Tag(
        name = "Users",
        description = "Operations including users"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/swApi/users")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Get a list of all the users",
            description = "Returns a list of UserDTO objects, featuring IDs and usernames of all the users"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "List of UserDTO returned successfully"
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
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return userService.getAllUsers();
    }

}
