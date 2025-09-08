package pl.coderslab.starwarsapp.requests;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class TeamCreateRequest {
    @Size(min = 3, max = 20)
    private String name;
    @Size(min = 4, max = 4)
    private List<Long> characters;
}
