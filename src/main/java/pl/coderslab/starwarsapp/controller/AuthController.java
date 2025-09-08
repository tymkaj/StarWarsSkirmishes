package pl.coderslab.starwarsapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.starwarsapp.requests.LoginRequest;
import pl.coderslab.starwarsapp.requests.RegisterRequest;
import pl.coderslab.starwarsapp.service.AuthService;

import java.util.Map;

@Tag(
        name = "Authorization",
        description = "Operations including authorization"
)
@RestController
@RequestMapping("/swApi/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Register",
            description = "Registers a user to the database"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "User registered succesfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request, registerRequest requires a username, password and an email"
            )
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @Operation(
            summary = "Log in",
            description = "Log in to the app"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User logged in succesfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request, loginRequest requires a username and password"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid authorization"
            )
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        return authService.login(request, httpRequest, httpResponse);
    }

    @Operation(
            summary = "Log out",
            description = "Logs out a user from the app"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User logged out succesfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized access or session expired"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access forbidden, logging out requires to be logged in first"
            )
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpRequest, HttpServletResponse response) {
        return authService.logout(httpRequest, response);
    }

    @Operation(
            summary = "Get session info",
            description = "Provides the username and userId of the logged in user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Session info provided successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized access or session expired"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access forbidden, providing session info requires a session to be created"
            )
    })
    @GetMapping("/session-info")
    public ResponseEntity<Map<String, Object>> getSessionInfo() {
        return authService.sessionInfo();
    }

    @Operation(
            summary = "Get CsrfToken",
            description = "Provides the CSRF token if available, or creates it"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Token returned successfully"
            )
    })
    @GetMapping("/csrf")
    public Map<String, String> csrf(CsrfToken token) {
        return Map.of("token", token.getToken());
    }

}
