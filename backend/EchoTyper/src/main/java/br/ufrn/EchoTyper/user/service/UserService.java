package br.ufrn.EchoTyper.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.EchoTyper.user.dto.UserResponseDTO;
import br.ufrn.EchoTyper.user.model.User;
import br.ufrn.EchoTyper.user.repository.UserRepository;
import br.ufrn.EchoTyper.user.dto.UserMapper;
import br.ufrn.EchoTyper.user.dto.UserRequestDTO;
import br.ufrn.EchoTyper.user.dto.UserMapper;

@Service
public class UserService {
    // TODO: Fazer o Exception Handling
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // TODO: Fazer o exception handling
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        if (userRepository.findByEmail(userRequestDTO.email()).isPresent()) {
            throw new IllegalArgumentException("Email already in use.");
        }
        if (userRepository.findByUsername(userRequestDTO.username()).isPresent()) {
            throw new IllegalArgumentException("Username already in use.");
        }
        User newUser = UserMapper.toEntity(userRequestDTO);
        userRepository.save(newUser);
        return UserMapper.toResponseDTO(newUser);
    }

    public UserResponseDTO getUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            throw new IllegalArgumentException("The username does not exist");
        }
        return UserMapper.toResponseDTO(userRepository.findByUsername(username).get());
    }

    public UserResponseDTO updateUser( Long id, UserRequestDTO userRequestDTO) {
        User user = userRepository.findById(id).get(); // Changed userRequestDTO.id() to id
        user.setUsername(userRequestDTO.username());
        user.setEmail(userRequestDTO.username());
        user.setPassword(userRequestDTO.username());
        userRepository.save(user);
        return UserMapper.toResponseDTO(user);
    }

    public UserResponseDTO getUserById(Long id) {
        // TODO: deve ser tratado como excecao?
        return userRepository.findById(id).map(UserMapper::toResponseDTO).orElse(null);
    }

    // TODO: Usar paginacao
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().parallelStream().map(UserMapper::toResponseDTO).toList();
    }

    public UserResponseDTO getFriendByUsername(String username) {
        if (userRepository.findFriendByUsername(username).isPresent()) {
            return UserMapper.toResponseDTO(userRepository.findFriendByUsername(username).get());
        }
        throw new IllegalArgumentException("No Friend with the matching username");
    }

    public List<UserResponseDTO> getAllFriends(Long id) {
        if (userRepository.findAllFriends(id).isPresent()) {
            return userRepository.findAllFriends(id).get().parallelStream().map(UserMapper::toResponseDTO).toList();
        }
        return new ArrayList<>();
    }

    public List<UserResponseDTO> addFriend(Long id, Long friendId) {
        User user = userRepository.findById(id).get();
        User friend = userRepository.findById(friendId).get();
        user.getFriends().add(friend);
        userRepository.save(user);
        return getAllFriends(id);
    }

    public void removeFriend(Long id, Long friendId) throws IllegalArgumentException {
        User user = userRepository.findById(id).get();
        Optional<User> friend = userRepository.findById(friendId);
        if (friend.isPresent()) {
            user.getFriends().remove(friend.get());
        } else {
            // TODO: Implementar nova runtime exception
            throw new IllegalArgumentException();
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}