package com.finale.ConferenceManagement;

import com.finale.ConferenceManagement.model.Conference;
import com.finale.ConferenceManagement.repository.ConferenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ConferenceControllerTest {

    @Autowired
    private ConferenceRepository conferenceRepository;

    private Conference conference1;
    private Conference conference2;
    private UUID uuid1;
    private UUID uuid2;

    @BeforeEach
    void setUp() {
        conferenceRepository.deleteAll();

        conference1 = new Conference(
                "TITLE: A test title",
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                LocalDateTime.of(2023, Month.MAY, 20, 9, 0),
                "LOCATION: Shanghai",
                "THEME: A strange theme",
                "FOCUS: A hilarious point",
                Map.of("KEYNOTE: A beautiful keynote", "SPEAKERS: James, Bill"),
                Map.of(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "Agenda: Let's have this goddamn conference."),
                "REGISTRATION: At the front gate.",
                List.of("HOUSE 1", "HOUSE 2"),
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                "GUIDEFORPAPER: Click the submission button",
                "GUIDEFORPRE: Click the submission button",
                List.of("Sponsor 1", "Sponsor 2"),
                List.of("Exhibitor 1", "Exhibitor 2"),
                "PHONENUMBER: 1-888-8888"
        );

        uuid1 = conference1.getId();

        conference2 = new Conference(
                "TITLE: A second test title",
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                LocalDateTime.of(2023, Month.MAY, 21, 9, 0),
                "LOCATION: Shanghai",
                "THEME: A second strange theme",
                "FOCUS: A second hilarious point",
                Map.of("KEYNOTE: A beautiful keynote", "SPEAKERS: James, Bill"),
                Map.of(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "Agenda: Let's have this goddamn conference."),
                "REGISTRATION: At the front gate.",
                List.of("HOUSE 1", "HOUSE 2"),
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                "GUIDEFORPAPER: Click the submission button",
                "GUIDEFORPRE: Click the submission button",
                List.of("Sponsor 1", "Sponsor 2"),
                List.of("Exhibitor 1", "Exhibitor 2"),
                "PHONENUMBER: 1-888-8888"
        );

        uuid2 = conference2.getId();

        conferenceRepository.save(conference1);
    }

    @Test
    void getAllConferences(@Autowired WebTestClient client) {
        final List<Conference> conf = client.get()
                .uri("/api/conference/")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Conference.class)
                .returnResult()
                .getResponseBody();
        assertEquals(List.of(conference1), conf);
    }

    @Test
    void getConferenceById(@Autowired WebTestClient client) {
        final Conference conf = client.get()
                .uri("/api/conference/" + uuid1.toString())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Conference.class)
                .returnResult()
                .getResponseBody();
        assertEquals(conference1, conf);
    }

    @Test
    void addConference(@Autowired WebTestClient client) {
        final Conference addedConf = client.post()
                .uri("/api/conference/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(conference2), Conference.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Conference.class)
                .returnResult()
                .getResponseBody();
        assertEquals(conference2, addedConf);
    }

    @Test
    void updateConference(@Autowired WebTestClient client) {
        final Conference updatedConf = client.put()
                .uri("/api/conference/" + uuid2.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(conference2), Conference.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Conference.class)
                .returnResult()
                .getResponseBody();
        assertEquals(conference2, updatedConf);
    }

    @Test
    void deleteConference(@Autowired WebTestClient client) {
        final Conference deletedConf1 = client.delete()
                .uri("/api/conference/" + uuid1.toString())
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Conference.class)
                .returnResult()
                .getResponseBody();
//        assertEquals(conference1, deletedConf1);
        assertFalse(conferenceRepository.findById(uuid1).isPresent());
//        final Void deletedConf2 = client.delete()
//                .uri("/api/conference/" + UUID.randomUUID())
//                .exchange()
//                .expectStatus().isNotFound()
//                .expectBody(Void.class)
//                .returnResult()
//                .getResponseBody();
    }
}