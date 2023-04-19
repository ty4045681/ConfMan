package com.finale.ConferenceManagement.service;

import com.finale.ConferenceManagement.model.ApplyStatus;
import com.finale.ConferenceManagement.model.Conference;
import com.finale.ConferenceManagement.model.Presentation;
import com.finale.ConferenceManagement.model.User;
import com.finale.ConferenceManagement.repository.ConferenceRepository;
import com.finale.ConferenceManagement.repository.PresentationRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class PresentationService {
    private final FileStorageService fileStorageService;
    private final PresentationRepository presentationRepository;
    private final ConferenceRepository conferenceRepository;
    private final UserService userService;
    private final PaperService paperService;

    public PresentationService(FileStorageService fileStorageService, PresentationRepository presentationRepository, ConferenceRepository conferenceRepository, UserService userService, PaperService paperService) {
        this.fileStorageService = fileStorageService;
        this.presentationRepository = presentationRepository;
        this.conferenceRepository = conferenceRepository;
        this.userService = userService;
        this.paperService = paperService;
    }

    public Presentation storeFile(String conferenceId,
                                  String paperId,
                                  String submitterName,
                                  String title,
                                  MultipartFile file) throws RuntimeException {
        Presentation presentation = new Presentation(
                getConferenceFromConferenceId(conferenceId),
                getUserFromUsername(submitterName),
                ApplyStatus.PENDING,
                Objects.requireNonNull(paperService.findPaperById(UUID.fromString(paperId)).orElse(null)),
                title,
                ""
        );

        return fileStorageService.storeFile(presentation, file, presentationRepository::save, (p, storedFileName) -> {
            p.setFileName(storedFileName);
            return p;
        }, (fileStorageLocation, fileName) -> fileStorageLocation.resolve("presentation").resolve(fileName));
    }

    public Resource loadFileAsResource(String fileName) {
        return fileStorageService.loadFileAsResource("presentation/" + fileName);
    }

    public Optional<Presentation> findPresentationById(UUID id) {
        return presentationRepository.findById(id);
    }

    public List<Presentation> findAllPresentationsByPaperId(UUID paperId) {
        return presentationRepository.findAllByPaperId(paperId);
    }

    public List<Presentation> findAllPresentationsByConferenceId(UUID conferenceId) {
        return presentationRepository.findAllByConferenceId(conferenceId);
    }

    public List<Presentation> findAllPresentationsByConferenceIdAndPaperId(UUID conferenceId, UUID paperId) {
        return presentationRepository.findAllByConferenceIdAndPaperId(conferenceId, paperId);
    }

    public List<Presentation> findAllPresentations() {
        return presentationRepository.findAll();
    }

    private Conference getConferenceFromConferenceId(String conferenceId) {
        return conferenceRepository.findConferenceById(UUID.fromString(conferenceId));
    }
    private User getUserFromUsername(String username) {
        return userService.findByUsername(username).orElse(null);
    }
}
