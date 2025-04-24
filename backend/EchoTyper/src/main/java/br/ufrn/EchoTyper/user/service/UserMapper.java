package br.ufrn.EchoTyper.user.service;

import br.ufrn.EchoTyper.user.dto.UserRequestDTO;
import br.ufrn.EchoTyper.user.dto.UserResponseDTO;
import br.ufrn.EchoTyper.user.model.User;

public class UserMapper {
    public static User toEntity(UserRequestDTO userRequestDTO) {
     return new User(null, userRequestDTO.password(), userRequestDTO.username(), userRequestDTO.email());
    }

    public static UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(user.getUsername(), user.getEmail());
    }
}
