package pl.coderslab.starwarsapp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class GameCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String role;
    private String affiliation;
    private String powerLvl;
    @ManyToMany(mappedBy = "characters")
    private List<Team> teams;
    private String imgUrl;
}
