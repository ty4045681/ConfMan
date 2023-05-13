package com.finale.ConferenceManagement.interfaces;

import com.finale.ConferenceManagement.model.Conference;
import com.finale.ConferenceManagement.model.User;

import java.util.Set;

public interface ReviewRepositoryCustom {
    long countJudgesByConference(Conference conference);
    Set<User> findJudgesByConference(Conference conference);
}
