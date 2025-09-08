package pl.coderslab.starwarsapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.coderslab.starwarsapp.dto.UserDTO;
import pl.coderslab.starwarsapp.mapper.UserMapper;
import pl.coderslab.starwarsapp.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll().stream()
                .map(userMapper::UserToUserDTO)
                .toList());
    }

}
