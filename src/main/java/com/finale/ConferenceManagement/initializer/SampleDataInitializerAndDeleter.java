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
    private final User judge1;
    private final User judge2;

    private final Conference conference1;
    private final Conference conference2;

    private final Attendance attendance1;
    private final Attendance attendance2;
    private final Attendance attendance3;
    private final Attendance attendance4;

    private final Review review1;

    private final Review review2;

    private final Paper paper1;
    private final Paper paper2;

    public SampleDataInitializerAndDeleter(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;

        user1 = new User(
                "USER1",
                "abc@gmail.com",
                "password1",
                Set.of(UserRole.USER),
                "Peter"
        );

        user2 = new User(
                "USER2",
                "def@gmail.com",
                "password2",
                Set.of(UserRole.ADMIN),
                "Sadie"
        );

        organizer1 = new User(
                "ORGANIZER1",
                "123@gmail.com",
                "password1",
                Set.of(UserRole.ORGANIZER),
                "Tom"
        );

        organizer2 = new User(
                "ORGANIZER2",
                "456@gmail.com",
                "password2",
                Set.of(UserRole.ORGANIZER),
                "Jerry"
        );

        judge1 = new User(
                "JUDGE1",
                "JUDGE1@gmail.com",
                "password1",
                Set.of(UserRole.JUDGE),
                "Tom"
        );

        judge2 = new User(
                "JUDGE2",
                "JUDGE2@gmail.com",
                "password2",
                Set.of(UserRole.JUDGE),
                "Jerry"
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
                ApplyStatus.APPROVED
        );

        attendance3 = new Attendance(
                user1,
                conference2,
                ApplyStatus.APPROVED
        );

        attendance4 = new Attendance(
                user2,
                conference2,
                ApplyStatus.APPROVED
        );

        paper1 = new Paper(
                conference1,
                user1,
                ApplyStatus.APPROVED,
                "title1",
                Set.of("Joe", "Tom"),
                "abstract1",
                Set.of("keyword1", "keyword2"),
                ""
        );

        paper2 = new Paper(
                conference2,
                user1,
                ApplyStatus.PENDING,
                "title2",
                Set.of("Jack", "Alice"),
                "abstract2",
                Set.of("keyword1", "keyword2"),
                ""
        );

        review1 = new Review(
                judge1,
                conference1,
                paper1,
                ApplyStatus.APPROVED
        );

        review2 = new Review(
                judge1,
                conference1,
                paper2,
                ApplyStatus.PENDING
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
                    mongoTemplate.insert(judge1, name);
                    mongoTemplate.insert(judge2, name);
                    System.out.println("Successfully insert user1 and user2");
                    System.out.println("Successfully insert organizer1 and organizer2");
                    System.out.println("Successfully insert judge1 and judge2");
                    System.out.println("USER1 id: " + user1.getId().toString());
                    System.out.println("USER2 id: " + user2.getId().toString());
                    System.out.println("ORGANIZER1 id: " + organizer1.getId().toString());
                    System.out.println("ORGANIZER2 id: " + organizer2.getId().toString());
                    System.out.println("JUDGE1 id: " + judge1.getId().toString());
                    System.out.println("JUDGE2 id: " + judge2.getId().toString());
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
                    mongoTemplate.insert(attendance3, name);
                    mongoTemplate.insert(attendance4, name);
                    System.out.println("Successfully insert attendance1, attendance2, attendance3 and attendance4");
                    System.out.println("ATTENDANCE1 id: " + attendance1.getId().toString());
                    System.out.println("ATTENDANCE2 id: " + attendance2.getId().toString());
                    System.out.println("ATTENDANCE3 id: " + attendance3.getId().toString());
                    System.out.println("ATTENDANCE4 id: " + attendance4.getId().toString());
                }
                case "paper" -> {
                    mongoTemplate.insert(paper1, name);
                    mongoTemplate.insert(paper2, name);
                    System.out.println("Successfully insert paper1, paper2");
                    System.out.println("PAPER1 title: " + paper1.getTitle());
                    System.out.println("PAPER2 title: " + paper2.getTitle());
                }
                case "review" -> {
                    mongoTemplate.insert(review1, name);
                    mongoTemplate.insert(review2, name);
                    System.out.println("Successfully insert review1, review2");
                    System.out.println("REVIEW1 id: " + review1.getId().toString());
                    System.out.println("REVIEW2 id: " + review2.getId().toString());
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
        mongoTemplate.remove(new Query(), "review");
        System.out.println("Delete all reviews in 'review' collection");
    }
}
