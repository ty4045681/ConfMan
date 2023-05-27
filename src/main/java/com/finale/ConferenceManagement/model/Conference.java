package com.finale.ConferenceManagement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Document(collection = "conference")
@Data
@AllArgsConstructor
public class Conference {
	// Identifier
    @Id
    private UUID id = UUID.randomUUID();

    // Foreign key
    @NonNull
    @DBRef
    private User organizer;

    // 1. Conference title
    @NonNull
    private String title;

    // 2. Date and location
    @NonNull
    private LocalDateTime startDate;
    @NonNull
    private LocalDateTime endDate;
        // TODO: Consider Google Maps API
    @NonNull
    private String location;

    // 3. Theme and focus
    @NonNull
    private String theme;
    @NonNull
    private String focus;

    // 4. Keynote speakers
    @NonNull
    private Map<String, String> keynotesAndSpeakers;


    // 5. Schedule and agenda
    @NonNull
    // TODO: Consider Google Calendar API
    private Map<LocalDateTime, String> agenda;

    // 6. Registration information
    @NonNull
    private String registrationInfo;

    // 7. Accommodations
    @NonNull
    // TODO: Consider Google Maps API
    private List<String> accommodations;

    // 8. Call for papers or presentations
    @NonNull
    private LocalDateTime startCallingDateForPapers;
    @NonNull
    private LocalDateTime endCallingDateForPapers;
    @NonNull
    private LocalDateTime startCallingDateForPresentations;
    @NonNull
    private LocalDateTime endCallingDateForPresentations;
    @NonNull
    private String guidelineForPaperSubmission;
    @NonNull
    private String guidelineForPresentationSubmission;

    // 9. Sponsors and exhibitors
    @NonNull
    private List<String> sponsors;
    @NonNull
    private List<String> exhibitors;

    // 10. Contact information
    @NonNull
    private String phoneNumber;

    @NonNull
    private ApplyStatus status;
}