package pl.coderslab.starwarsapp.seeder;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pl.coderslab.starwarsapp.entity.GameCharacter;
import pl.coderslab.starwarsapp.repository.GameCharacterRepository;

@Component
@RequiredArgsConstructor
public class GameCharacterSeeder implements ApplicationRunner {

    private final GameCharacterRepository gameCharacterRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seed("Darth Vader", "The Chosen One", "Empire", "95", "/images/Vader.jpg");
        seed("Luke Skywalker", "Jedi Knight", "Rebels", "95", "/images/Luke.jpg");
        seed("Mace Windu", "Jedi Master", "Republic", "90", "/images/Windu.jpg");
        seed("Darth Sidious", "The Emperor", "Empire", "100", "/images/Sidious.jpg");
        seed("Stormtrooper", "Marksman", "Empire", "20", "/images/Stormtrooper.jpg");
        seed("Rebel", "Marksman", "Rebels", "20", "/images/Rebel.jpg");
        seed("Boba Fett", "Bounty Hunter", "Neutral", "80", "/images/BobaFett.jpg");
        seed("Princess Leia", "Support", "Rebels", "75", "/images/PrincessLeia.jpg");
    }

    private void seed(String name, String role, String affiliation, String lvl, String img) {
        gameCharacterRepository.findByName(name).orElseGet(() ->
                gameCharacterRepository.save(GameCharacter.builder()
                        .name(name)
                        .role(role)
                        .affiliation(affiliation)
                        .powerLvl(lvl)
                        .imgUrl(img)
                        .build())
        );
    }

}
