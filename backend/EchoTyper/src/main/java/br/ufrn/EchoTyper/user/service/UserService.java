package br.ufrn.EchoTyper.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufrn.EchoTyper.user.dto.UserResponseDTO;
import br.ufrn.EchoTyper.user.model.User;
import br.ufrn.EchoTyper.user.repository.UserRepository;
import br.ufrn.EchoTyper.user.dto.UserRequestDTO;
import br.ufrn.EchoTyper.user.dto.UserMapper;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Fazer o exception handling
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
        return userRepository.findById(id).map(UserMapper::toResponseDTO).orElse(null);
    }

    public List<UserResponseDTO> getAllUsers() {
        // TODO: Usar paginacao
        return userRepository.findAll().parallelStream().map(UserMapper::toResponseDTO).toList();
    }

    public UserResponseDTO getFriendByUsername(String username) {
        if (userRepository.findFriendByUsername(username).isPresent()) {
            return UserMapper.toResponseDTO(userRepository.findFriendByUsername(username).get());
        }
        return null;
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