package pl.coderslab.starwarsapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
    @NotBlank
    private String password;
    @Email
    private String email;
    @OneToMany(mappedBy = "user")
    private List<Team> teams =  new ArrayList<>();
}
