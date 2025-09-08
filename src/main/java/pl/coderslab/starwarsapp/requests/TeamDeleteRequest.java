package pl.coderslab.starwarsapp.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TeamDeleteRequest {
    @NotNull
    private Long id;
    @Size(min = 3, max = 20)
    private String name;
}
