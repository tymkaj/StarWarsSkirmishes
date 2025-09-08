package pl.coderslab.starwarsapp.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
    @NotBlank
    private String password;
}
