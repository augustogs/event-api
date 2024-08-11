package com.eventAPI.service;

import com.eventAPI.dto.UserDTO;
import com.eventAPI.exception.UserAlreadyRegisteredException;
import com.eventAPI.model.User;
import com.eventAPI.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUserSuccess() {
        UserDTO userDTO = new UserDTO(null, "Augusto", "augusto@email.com");
        User userToSave = new User(userDTO.getName(), userDTO.getEmail());
        User savedUser = new User(userDTO.getName(), userDTO.getEmail());
        savedUser.setId(1L);

        when(userRepository.findByEmail("augusto@email.com")).thenReturn(Optional.empty());
        when(userRepository.save(userToSave)).thenReturn(savedUser);

        UserDTO createdUser = userService.createUser(userDTO);

        assertNotNull(createdUser.getId());
        assertEquals("Augusto", createdUser.getName());
        assertEquals("augusto@email.com", createdUser.getEmail());

        verify(userRepository, times(1)).findByEmail("augusto@email.com");
        verify(userRepository, times(1)).save(userToSave);
    }

    @Test
    void testCreateUserAlreadyRegistered() {
        UserDTO userDTO = new UserDTO(null, "Augusto", "augusto@email.com");
        User existingUser = new User("Augusto", "augusto@email.com");

        when(userRepository.findByEmail("augusto@email.com")).thenReturn(Optional.of(existingUser));

        UserAlreadyRegisteredException exception = assertThrows(UserAlreadyRegisteredException.class, () -> {
            userService.createUser(userDTO);
        });

        assertEquals("User is already registered!", exception.getMessage());

        verify(userRepository, times(1)).findByEmail("augusto@email.com");
        verify(userRepository, times(0)).save(any(User.class));
    }
}
