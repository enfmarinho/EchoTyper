package br.ufrn.EchoTyper.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.EchoTyper.user.dto.UserRequestDTO;
import br.ufrn.EchoTyper.user.dto.UserResponseDTO;
import jakarta.validation.Valid;

@RequestMapping("/user")
public interface UserController {
    @GetMapping("")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers();

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("id") Long id);

    @GetMapping("/name/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable("username") String username);

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<UserResponseDTO>> getAllFriends(@PathVariable Long id);

    @PostMapping("/create")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO createUserDTO);

    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(Long id, @Valid @RequestBody UserRequestDTO updateUserDTO);

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<UserResponseDTO> deleteUser(@PathVariable("id") Long id);

    @PostMapping("/addfriend/{id}/{friendId}")
    public ResponseEntity<List<UserResponseDTO>> addFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId);
}
