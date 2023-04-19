package com.finale.ConferenceManagement.initializer;

import com.finale.ConferenceManagement.model.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class SampleDataInitializerAndDeleter {
    private final MongoTemplate mongoTemplate;

    private final User user1;
    private final User user2;
    private final User organizer1;
    private final User organizer2;

    private final Conference conference1;
    private final Conference conference2;

    private final Attendance attendance1;
    private final Attendance attendance2;

    public SampleDataInitializerAndDeleter(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;

        user1 = new User(
                "USER1",
                "abc@gmail.com",
                "password1",
                Set.of(UserRole.USER, UserRole.ATTENDEE),
                "Peter",
                Set.of()
        );

        user2 = new User(
                "USER2",
                "def@gmail.com",
                "password2",
                Set.of(UserRole.USER, UserRole.ADMIN),
                "Sadie",
                Set.of()
        );

        organizer1 = new User(
                "ORGANIZER1",
                "123@gmail.com",
                "password1",
                Set.of(UserRole.ORGANIZER),
                "Tom",
                Set.of()
        );

        organizer2 = new User(
                "ORGANIZER2",
                "456@gmail.com",
                "password2",
                Set.of(UserRole.ORGANIZER),
                "Jerry",
                Set.of()
        );

        conference1 = new Conference(
                organizer1,
                "Tech Summit 2023",
                LocalDateTime.of(2023, 6, 1, 9, 0),
                LocalDateTime.of(2023, 6, 3, 18, 0),
                "San Francisco, CA",
                "Technology",
                "Innovation in Software",
                Map.of(
                        "Keynote 1", "John Doe",
                        "Keynote 2", "Jane Smith"
                ),
                Map.of(
                        LocalDateTime.of(2023, 6, 1, 10, 0), "Session 1",
                        LocalDateTime.of(2023, 6, 1, 11, 0), "Session 2"
                ),
                "Register online at techsummit2023.com",
                List.of("Hotel 1", "Hotel 2"),
                LocalDateTime.of(2023, 3, 1, 0, 0),
                LocalDateTime.of(2023, 4, 1, 0, 0),
                LocalDateTime.of(2023, 4, 1, 0, 0),
                LocalDateTime.of(2023, 5, 1, 0, 0),
                "Paper submission guidelines available at techsummit2023.com/cfp",
                "Presentation submission guidelines available at techsummit2023.com/cfp",
                List.of("Sponsor 1", "Sponsor 2"),
                List.of("Exhibitor 1", "Exhibitor 2"),
                "+1-800-123-4567"
        );

        conference2 = new Conference(
                organizer2,
                "Global Health Conference 2023",
                LocalDateTime.of(2023, 7, 15, 9, 0),
                LocalDateTime.of(2023, 7, 17, 18, 0),
                "London, UK",
                "Healthcare",
                "Global Health Challenges",
                Map.of(
                        "Keynote 1", "Dr. Alice Johnson",
                        "Keynote 2", "Dr. Bob Martin"
                ),
                Map.of(
                        LocalDateTime.of(2023, 7, 15, 10, 0), "Session 1",
                        LocalDateTime.of(2023, 7, 15, 11, 0), "Session 2"
                ),
                "Register online at globalhealthconference2023.com",
                List.of("Hotel A", "Hotel B"),
                LocalDateTime.of(2023, 4, 15, 0, 0),
                LocalDateTime.of(2023, 5, 15, 0, 0),
                LocalDateTime.of(2023, 5, 15, 0, 0),
                LocalDateTime.of(2023, 6, 15, 0, 0),
                "Paper submission guidelines available at globalhealthconference2023.com/cfp",
                "Presentation submission guidelines available at globalhealthconference2023.com/cfp",
                List.of("Sponsor A", "Sponsor B"),
                List.of("Exhibitor A", "Exhibitor B"),
                "+44-800-123-4567"
        );

        attendance1 = new Attendance(
                user1,
                conference1,
                ApplyStatus.PENDING
        );

        attendance2 = new Attendance(
                user2,
                conference1,
                ApplyStatus.ACCEPTED
        );

    }

    public void insertData(String... collectionName) {
        for (String name : collectionName) {
            switch (name) {
                case "user" -> {
                    mongoTemplate.insert(user1, name);
                    mongoTemplate.insert(user2, name);
                    mongoTemplate.insert(organizer1, name);
                    mongoTemplate.insert(organizer2, name);
                    System.out.println("Successfully insert user1 and user2");
                    System.out.println("Successfully insert organizer1 and organizer2");
                    System.out.println("USER1 id: " + user1.getId().toString());
                    System.out.println("USER2 id: " + user2.getId().toString());
                    System.out.println("ORGANIZER1 id: " + organizer1.getId().toString());
                    System.out.println("ORGANIZER2 id: " + organizer2.getId().toString());
                }
                case "conference" -> {
                    mongoTemplate.insert(conference1, name);
                    mongoTemplate.insert(conference2, name);
                    System.out.println("Successfully insert conference1 and conference2");
                    System.out.println("CONFERENCE1 title: " + conference1.getTitle());
                    System.out.println("CONFERENCE2 title: " + conference2.getTitle());
                }
                case "attendance" -> {
                    mongoTemplate.insert(attendance1, name);
                    mongoTemplate.insert(attendance2, name);
                    System.out.println("Successfully insert attendance1 and attendance2");
                    System.out.println("ATTENDANCE1 id: " + attendance1.getId().toString());
                    System.out.println("ATTENDANCE2 id: " + attendance2.getId().toString());
                }
                default -> System.out.println("Collection '" + name + "' does not exist");
            }
        }
    }

    public void deleteAllData() {
        mongoTemplate.remove(new Query(), "user");
        System.out.println("Delete all users in 'user' collection");
        mongoTemplate.remove(new Query(), "conference");
        System.out.println("Delete all conferences in 'conference' collection");
        mongoTemplate.remove(new Query(), "paper");
        System.out.println("Delete all papers in 'paper' collection");
        mongoTemplate.remove(new Query(), "presentation");
        System.out.println("Delete all presentations in 'presentation' collection");
        mongoTemplate.remove(new Query(), "attendance");
        System.out.println("Delete all attendances in 'attendance' collection");
    }
}
