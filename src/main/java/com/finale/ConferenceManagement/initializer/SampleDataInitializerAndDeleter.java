package com.finale.ConferenceManagement.initializer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.finale.ConferenceManagement.model.ApplyStatus;
import com.finale.ConferenceManagement.model.Attendance;
import com.finale.ConferenceManagement.model.Conference;
import com.finale.ConferenceManagement.model.Paper;
import com.finale.ConferenceManagement.model.Review;
import com.finale.ConferenceManagement.model.User;
import com.finale.ConferenceManagement.model.UserRole;

import net.datafaker.Faker;

@Component
public class SampleDataInitializerAndDeleter {
    private final MongoTemplate mongoTemplate;
    private final Faker faker;

    public SampleDataInitializerAndDeleter(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.faker = new Faker();
    }

    private Stream<User> generateUser(UserRole role) {
        return faker.stream(() -> {
                return new User(
                    faker.zelda().character(), 
                    faker.harryPotter().character().replace(" ", "_") + "@gmail.com", 
                    faker.expression("#{regexify '[a-z]{8,10}'}"), 
                    role, 
                    faker.funnyName().name()
                    // faker.address().fullAddress(),
                    // faker.phoneNumber().cellPhone(),
                    // faker.harryPotter().quote()
                );
        }).generate();
    }

    private Stream<Conference> generateConference(User organizer, ApplyStatus status) {
        if (organizer.getRole() != UserRole.ORGANIZER) {
            throw new IllegalArgumentException("The user is not an organizer");
        }

        return faker.stream((Supplier<Conference>) () -> {
            int year = faker.number().numberBetween(2021, 2025);
            LocalDateTime startDate = LocalDateTime.of(year, faker.number().numberBetween(1, 13), faker.number().numberBetween(1, 29), 0, 0);
            int duration = faker.number().numberBetween(1, 16);
            LocalDateTime endDate = startDate.plusDays(duration);
            String theme = faker.programmingLanguage().name();
            
            return new Conference(
                UUID.randomUUID(), 
                organizer,
                "Conference " + Integer.toString(year) + ": Topic in " + theme,
                startDate,
                endDate,
                faker.address().fullAddress(),
                theme,
                faker.pokemon().name(),
                Map.of(
                    faker.hobby().activity(), faker.zelda().character(),
                    faker.halfLife().character(), faker.zelda().character()
                ),
                Map.of(
                    startDate, faker.harryPotter().quote()
                ),
                "Register now to get the early bird discount!",
                List.of(
                    faker.address().fullAddress()
                ),
                startDate,
                endDate,
                startDate,
                endDate,
                faker.harryPotter().quote(),
                faker.harryPotter().quote(),
                List.of(
                    faker.harryPotter().character(),
                    faker.minecraft().animalName()
                ),
                List.of(
                    faker.harryPotter().character(),
                    faker.minecraft().animalName()
                ),
                faker.phoneNumber().cellPhone(),
                status
            );
        }).generate();
    }

    private Paper generatePaper(User author, Conference conference, ApplyStatus status) {
        if (author.getRole() != UserRole.USER) {
            throw new IllegalArgumentException("The user is not an author");
        }

        return new Paper(
            conference,
            author,
            status,
            faker.computer().brand(),
            Set.of(faker.name().firstName(), faker.name().firstName()),
            faker.minecraft().entityName(),
            Set.of(
                faker.minecraft().entityName(),
                faker.minecraft().animalName()
            ),
            ""
        );
    }

    private Review generateReview(User judge, Conference conference, Paper paper, ApplyStatus status) {
        if (judge.getRole() != UserRole.JUDGE) {
            throw new IllegalArgumentException("The user is not a judge");
        }

        return new Review(judge, conference, paper, status, faker.harryPotter().quote());
    }

    private Attendance generateAttendance(User user, Conference conference, ApplyStatus status) {
        if (user.getRole() != UserRole.USER) {
            throw new IllegalArgumentException("The user is not an attendee");
        }

        return new Attendance(UUID.randomUUID(), user, conference, status, faker.harryPotter().quote());
    }

    public void insertData() {
        System.out.println("\n++++++++++++++Inserting sample data...+++++++++++++++");
        List<User> attendees = generateUser(UserRole.USER).limit(10).toList();
        List<User> judges = generateUser(UserRole.JUDGE).limit(10).toList();
        List<User> organizers = generateUser(UserRole.ORGANIZER).limit(10).toList();
        List<User> admins = generateUser(UserRole.ADMIN).limit(10).toList();

        User sampleOrganizer1 = organizers.get(0);
        System.out.println("Organizer1 username: " + sampleOrganizer1.getUsername() + ", and password: " + sampleOrganizer1.getPassword());
        User sampleJudge1 = judges.get(0);
        System.out.println("Judge1 username: " + sampleJudge1.getUsername() + ", and password: " + sampleJudge1.getPassword());
        User sampleAttendee1 = attendees.get(0);
        System.out.println("Attendee1 username: " + sampleAttendee1.getUsername() + ", and password: " + sampleAttendee1.getPassword());
        User sampleAdmin1 = admins.get(0);
        System.out.println("Admin1 username: " + sampleAdmin1.getUsername() + ", and password: " + sampleAdmin1.getPassword());

        mongoTemplate.insert(attendees, "user");
        mongoTemplate.insert(judges, "user");
        mongoTemplate.insert(organizers, "user");
        mongoTemplate.insert(sampleAdmin1, "user");

        List<Conference> approvedConferences = generateConference(sampleOrganizer1, ApplyStatus.APPROVED).limit(10).toList();
        mongoTemplate.insert(approvedConferences, "conference");

        Conference sampleConference1 = approvedConferences.get(0);
        Conference sampleConference2 = approvedConferences.get(1);
        Attendance attendee1ToConference1 = generateAttendance(sampleAttendee1, sampleConference1, ApplyStatus.APPROVED);
        Attendance attendance1ToConference2 = generateAttendance(sampleAttendee1, sampleConference2, ApplyStatus.PENDING);
        Paper attendee1WritePaper1 = generatePaper(sampleAttendee1, sampleConference1, ApplyStatus.APPROVED);
        Paper attendee1WritePaper2 = generatePaper(sampleAttendee1, sampleConference2, ApplyStatus.PENDING);
        Review judge1ReviewPaper1 = generateReview(sampleJudge1, sampleConference1, attendee1WritePaper1, ApplyStatus.APPROVED);

        mongoTemplate.insert(attendee1ToConference1, "attendance");
        mongoTemplate.insert(attendance1ToConference2, "attendance");
        mongoTemplate.insert(attendee1WritePaper1, "paper");
        mongoTemplate.insert(attendee1WritePaper2, "paper");
        mongoTemplate.insert(judge1ReviewPaper1, "review");

        System.out.println("++++++++++++++Sample data inserted!+++++++++++++++");
    }

    public void deleteAllData() {
        System.out.println("\n+--------------Deleting all data in database--------------+");
        mongoTemplate.remove(new Query(), "user");
        System.out.println("| Delete all users in 'user' collection                   |");
        mongoTemplate.remove(new Query(), "conference");
        System.out.println("| Delete all conferences in 'conference' collection       |");
        mongoTemplate.remove(new Query(), "paper");
        System.out.println("| Delete all papers in 'paper' collection                 |");
        mongoTemplate.remove(new Query(), "presentation");
        System.out.println("| Delete all presentations in 'presentation' collection   |");
        mongoTemplate.remove(new Query(), "attendance");
        System.out.println("| Delete all attendances in 'attendance' collection       |");
        mongoTemplate.remove(new Query(), "review");
        System.out.println("| Delete all reviews in 'review' collection               |");

        System.out.println("+--------------All data in database deleted---------------+");
    }
}