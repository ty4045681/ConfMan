package com.finale.ConferenceManagement.service;

import com.finale.ConferenceManagement.exceptions.UserNotFoundException;
import com.finale.ConferenceManagement.model.ApplyStatus;
import com.finale.ConferenceManagement.model.Conference;
import com.finale.ConferenceManagement.model.Paper;
import com.finale.ConferenceManagement.model.User;
import com.finale.ConferenceManagement.repository.ConferenceRepository;
import com.finale.ConferenceManagement.repository.PaperRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaperService {
    private final FileStorageService fileStorageService;
    private final PaperRepository paperRepository;
    private final ConferenceRepository conferenceRepository;
    private final UserService userService;

    public PaperService(FileStorageService fileStorageService, PaperRepository paperRepository, ConferenceRepository conferenceRepository, UserService userService) {
        this.fileStorageService = fileStorageService;
        this.paperRepository = paperRepository;
        this.conferenceRepository = conferenceRepository;
        this.userService = userService;
    }

    public Paper storeFile(String conferenceId,
                           String submitterName,
                           String title,
                           String authors,
                           String abstractOfPaper,
                           String keywords,
                           MultipartFile file) throws RuntimeException {


        Paper paper = new Paper(
                getConferenceFromConferenceId(conferenceId),
                getUserFromUsername(submitterName),
                ApplyStatus.PENDING,
                title,
                Arrays.stream(authors.split(",")).collect(Collectors.toSet()),
                abstractOfPaper,
                Arrays.stream(keywords.split(",")).collect(Collectors.toSet()),
                ""
        );

        return fileStorageService.storeFile(paper, file, paperRepository::save, (p, storedFileName) -> {
            p.setFileName(storedFileName);
            return p;
        }, ((fileStorageLocation, fileName) -> fileStorageLocation.resolve("paper").resolve(fileName)));
    }

    public Resource loadFileAsResource(String fileName) {
        return fileStorageService.loadFileAsResource("paper/" + fileName);
    }

    public Optional<Paper> findPaperById(UUID id) {
        return paperRepository.findById(id);
    }

    public List<Paper> findAllPapers() {
        return paperRepository.findAll();
    }

    public List<Paper> findAllPapersByConferenceId(String id) {
        return paperRepository.findAllByConferenceId(UUID.fromString(id));
    }

    public List<Paper> findAllPapersByUsername(String username) {
        User user = getUserFromUsername(username);
        return paperRepository.findAllByAuthor(user);
    }

    public List<Paper> findAllPapersByUserId(String id) {
        User user = getUserFromUserId(id);
        if (user.equals(null)) {
            throw new UserNotFoundException();
        }
        return paperRepository.findAllByAuthor(user);
    }

    public long countPaperByAuthor(String username) {
        return paperRepository.countByAuthor(getUserFromUsername(username));
    }

    public long countPaperByAuthorAndStatus(String username, ApplyStatus status) {
        return paperRepository.countByAuthorAndStatus(getUserFromUsername(username), status);
    }

    public long countPaperByAuthorAndStatusAndConferenceTime(String username, ApplyStatus status, boolean isConferenceUpcoming) {
        return paperRepository.countByAuthorAndStatusAndConferenceTime(getUserFromUsername(username), status, isConferenceUpcoming);
    }

    public long countPapersByConference(String conferenceId) {
        return paperRepository.countByConference(getConferenceFromConferenceId(conferenceId));
    }

    public long countPapersByConference(Conference conference) {
        return paperRepository.countByConference(conference);
    }

    private Conference getConferenceFromConferenceId(String conferenceId) {
        return conferenceRepository.findConferenceById(UUID.fromString(conferenceId));
    }

    private User getUserFromUsername(String username) {
        return userService.findByUsername(username).orElse(null);
    }

    private User getUserFromUserId(String id) {
        return userService.findById(UUID.fromString(id)).orElse(null);
    }

}
