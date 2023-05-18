package com.finale.ConferenceManagement;

import com.finale.ConferenceManagement.model.User;
import com.finale.ConferenceManagement.model.UserRole;
import com.finale.ConferenceManagement.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserControllerTest {

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private UUID uuid1;
    private UUID uuid2;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        user1 = new User(
                "USER 1",
                "abc@gmail.com",
                "password1",
                UserRole.USER,
                "Peter"
        );

        uuid1 = user1.getId();

        user2 = new User(
                "USER 2",
                "def@gmail.com",
                "password2",
                UserRole.ADMIN,
                "Sadie"
        );

        uuid2 = user2.getId();

        userRepository.save(user1);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllUsers(@Autowired WebTestClient client) {
        final List<User> users = client.get()
                .uri("/api/user/")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .returnResult()
                .getResponseBody();
        assertEquals(List.of(user1), users);
    }

    @Test
    void getUserById(@Autowired WebTestClient client) {
        final User user = client.get()
                .uri("/api/user/" + uuid1.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .returnResult()
                .getResponseBody();
        assertEquals(user1, user);
    }

    @Test
    void addUser(@Autowired WebTestClient client) {
        final User addedUser = client.post()
                .uri("/api/user/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(user2), User.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(User.class)
                .returnResult().getResponseBody();
        assertEquals(user2, addedUser);
    }

    @Test
    void updateUser(@Autowired WebTestClient client) {
        final User updatedUser = client.put()
                .uri("/api/user/" + uuid2.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(user2), User.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .returnResult()
                .getResponseBody();
        assertEquals(user2, updatedUser);
    }

    @Test
    void deleteUser(@Autowired WebTestClient client) {
        final User deletedUser = client.delete()
                .uri("/api/user/" + uuid1.toString())
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(User.class)
                .returnResult().getResponseBody();
//        assertEquals(user1, deletedUser);
        assertFalse(userRepository.findById(uuid1).isPresent());
    }
}