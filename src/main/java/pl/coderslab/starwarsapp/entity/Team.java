package pl.coderslab.starwarsapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 3, max = 20)
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToMany
    @Size(min = 4, max = 4)
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private List<GameCharacter> characters =  new ArrayList<>();
    @EqualsAndHashCode.Exclude
    private Boolean deleted = false;
}
