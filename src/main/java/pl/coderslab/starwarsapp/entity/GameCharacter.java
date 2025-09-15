package pl.coderslab.starwarsapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String role;
    private String affiliation;
    private String powerLvl;
    @ManyToMany(mappedBy = "characters")
    @Builder.Default
    @ToString.Exclude @EqualsAndHashCode.Exclude
    private List<Team> teams =  new ArrayList<>();
    private String imgUrl;
}
