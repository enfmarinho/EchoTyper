package br.ufrn.EchoTyper.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.EchoTyper.user.service.UserService;
import br.ufrn.EchoTyper.user.dto.UserResponseDTO;
import io.micrometer.core.ipc.http.HttpSender.Response;
import br.ufrn.EchoTyper.user.dto.UserRequestDTO;

@RestController
public class UserControllerImpl implements UserController {
    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<UserResponseDTO> createUser(UserRequestDTO createUserDTO) {
        return ResponseEntity.ok().body(userService.createUser(createUserDTO));
    }

    @Override
    public ResponseEntity<UserResponseDTO> updateUser(Long id, UserRequestDTO updateUserDTO) {
        return ResponseEntity.ok().body(userService.updateUser(id, updateUserDTO));
    }

    @Override
    public ResponseEntity deleteUser(Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @Override
    public ResponseEntity<UserResponseDTO> getUserById(Long id) {
        return ResponseEntity.ok().body(userService.getUserById(id));
    }

    @Override
    public ResponseEntity<UserResponseDTO> getUserByUsername(String username) {
        return ResponseEntity.ok().body(userService.getUserByUsername(username));
    }

    @Override
    public ResponseEntity<List<UserResponseDTO>> getAllFriends(Long id) {
        return ResponseEntity.ok().body(userService.getAllFriends(id));
    }

    @Override
    public ResponseEntity<List<UserResponseDTO>> addFriend(Long id, Long friendId) {
        return ResponseEntity.ok().body(userService.addFriend(id, friendId));
    }

}