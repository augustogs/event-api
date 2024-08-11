package com.eventAPI.service;

import com.eventAPI.dto.UserDTO;
import com.eventAPI.exception.UserAlreadyRegisteredException;
import com.eventAPI.model.User;
import com.eventAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO createUser(UserDTO userDTO) {
        Optional<User> userOpt = userRepository.findByEmail(userDTO.getEmail());
        if (userOpt.isPresent()) {
            throw new UserAlreadyRegisteredException("User is already registered!");
        }
        User user = new User(userDTO.getName(), userDTO.getEmail());
        user = userRepository.save(user);
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }
}
