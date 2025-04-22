package br.ufrn.EchoTyper.user;

import br.ufrn.EchoTyper.user.dto.UserRequestDTO;
import br.ufrn.EchoTyper.user.dto.UserResponseDTO;

public class UserMapper {
    public static User toEntity(UserRequestDTO userRequestDTO) {
     return new User(null, userRequestDTO.username(),userRequestDTO.email(), userRequestDTO.password());
    }

    public static UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(user.getUsername(), user.getEmail());
    }
}
