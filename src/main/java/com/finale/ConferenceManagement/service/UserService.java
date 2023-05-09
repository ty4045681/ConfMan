package com.finale.ConferenceManagement.service;

import com.finale.ConferenceManagement.dto.RegisterRequest;
import com.finale.ConferenceManagement.dto.RegisterResponse;
import com.finale.ConferenceManagement.dto.UpdateRequest;
import com.finale.ConferenceManagement.exceptions.UserAlreadyExistsException;
import com.finale.ConferenceManagement.model.User;
import com.finale.ConferenceManagement.model.UserRole;
import com.finale.ConferenceManagement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("Email already in use");
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("Username already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email already in use");
        }

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException("Username already in use");
        }

        User newUser = new User(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                Set.of(UserRole.USER),
                registerRequest.getName()
        );

        if (!registerRequest.getAddress().isEmpty()) {
            newUser.setAddress(registerRequest.getAddress());
        }
        if (!registerRequest.getPhoneNumber().isEmpty()) {
            newUser.setPhoneNumber(registerRequest.getPhoneNumber());
        }
        if (!registerRequest.getBio().isEmpty()) {
            newUser.setBio(registerRequest.getBio());
        }

        User savedUser = userRepository.save(newUser);

        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setUsername(savedUser.getUsername());
        registerResponse.setEmail(savedUser.getEmail());

        return registerResponse;
    }

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> deleteById(UUID id) {
        Optional<User> optionalUser = findById(id);
        if (optionalUser.isPresent()) {
            userRepository.deleteById(id);
            return optionalUser;
        } else {
            return Optional.empty();
        }
    }
    public Optional<User> updateUser(String id, UpdateRequest updateRequest) {
        Optional<User> optionalUser = userRepository.findById(UUID.fromString(id));
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            user.setUsername(updateRequest.getUsername());
            user.setEmail(updateRequest.getEmail());
            user.setName(updateRequest.getName());
            user.setAddress(updateRequest.getAddress());
            user.setPhoneNumber(updateRequest.getPhoneNumber());
            user.setBio(updateRequest.getBio());

            userRepository.save(user);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public boolean checkUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean changePassword(String id, String currentPassword, String newPassword) {
        Optional<User> optionalUser = userRepository.findById(UUID.fromString(id));
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (currentPassword.equals(user.getPassword())) {
                user.setPassword(newPassword);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
}
