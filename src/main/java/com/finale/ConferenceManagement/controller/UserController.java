package com.finale.ConferenceManagement.controller;

import com.finale.ConferenceManagement.dto.*;
import com.finale.ConferenceManagement.model.User;
import com.finale.ConferenceManagement.service.UserService;
import com.finale.ConferenceManagement.util.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private UserService userService;
    private JwtUtils jwtUtils;

//    @GetMapping
//    public ResponseEntity<List<User>> getAllUsers() {
//        List<User> userList = userRepository.findAll();
//        return new ResponseEntity<>(userList, HttpStatus.OK);
//    }

    // If the request header has JWT
    @GetMapping
    public ResponseEntity<?> getUser(@RequestHeader("Authorization") String token,
                                                   @RequestHeader(value = "AllInfo", required = false) boolean isAllInfo) {
        String jwtToken = jwtUtils.getJwtFromAuthHeader(token);
        String username = jwtUtils.getUsernameFromToken(jwtToken);
        Optional<User> user = userService.findByUsername(username);

        if (isAllInfo) {
            return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } else {
            return user.map(value -> new ResponseEntity<>(new GetUserResponse(value.getId().toString(), value.getUsername()), HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    }

    @GetMapping("id={id}")
    public ResponseEntity<GetUserByIdResponse> getUserById(@PathVariable("id") String id) {
        UUID uuid = UUID.fromString(id);
        Optional<User> user = userService.findById(uuid);
        return user.map(value -> new ResponseEntity<>(new GetUserByIdResponse(value.getUsername()), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("username={username}")
    public ResponseEntity<GetUserByUsernameResponse> getUserByUsername(@PathVariable("username") String username) {
        Optional<User> user = userService.findByUsername(username);
        return user.map(value -> new ResponseEntity<>(new GetUserByUsernameResponse(value.getId(), value.getUsername()), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User savedUser = userService.registerUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PutMapping("id={id}")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("id={id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") String id) {
        Optional<User> optionalUser = userService.deleteById(UUID.fromString(id));
        return optionalUser.map(user -> new ResponseEntity<>(user, HttpStatus.NO_CONTENT)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@Validated @RequestBody RegisterRequest registerRequest) {
        RegisterResponse registerResponse = userService.registerUser(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }

    @PostMapping("/check-username")
    public ResponseEntity<?> checkUsername(@RequestParam("username") String username) {
        boolean exists = userService.checkUsername(username);
        return ResponseEntity.ok().body(new CheckResponse(exists));
    }

    @PostMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam("email") String email) {
        boolean exists = userService.checkEmail(email);
        return ResponseEntity.ok().body(new CheckResponse(exists));
    }
}
