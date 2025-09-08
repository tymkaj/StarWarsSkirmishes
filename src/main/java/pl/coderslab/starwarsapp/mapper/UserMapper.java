package pl.coderslab.starwarsapp.mapper;

import org.mapstruct.Mapper;
import pl.coderslab.starwarsapp.dto.UserDTO;
import pl.coderslab.starwarsapp.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO UserToUserDTO(User user);
    User UserDTOToUser(UserDTO userDTO);

}
