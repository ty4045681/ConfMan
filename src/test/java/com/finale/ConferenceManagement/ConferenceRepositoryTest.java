package com.finale.ConferenceManagement;

import com.finale.ConferenceManagement.model.Conference;
import com.finale.ConferenceManagement.repository.ConferenceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
public class ConferenceRepositoryTest {
    @Autowired
    private ConferenceRepository conferenceRepository;
    private Conference conference1, conference2;

    @BeforeEach
    void setUp() {
        conference1 = new Conference(
                null,"TITLE: A test title",
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

        conference2 = new Conference(
                null, "TITLE: A second test title",
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

        conferenceRepository.saveAll(List.of(conference1, conference2));
    }

    @AfterEach
    void tearDown() {
        conferenceRepository.deleteAll();
    }

    @Test
    void testFindAll() {
        assertEquals(conferenceRepository.findAll(), List.of(conference1, conference2));
    }
}
