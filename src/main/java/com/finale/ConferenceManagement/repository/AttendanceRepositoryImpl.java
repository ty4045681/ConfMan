package com.finale.ConferenceManagement.repository;

import com.finale.ConferenceManagement.interfaces.AttendanceRepositoryCustom;
import com.finale.ConferenceManagement.model.ApplyStatus;
import com.finale.ConferenceManagement.model.Conference;
import com.finale.ConferenceManagement.model.User;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class AttendanceRepositoryImpl implements AttendanceRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    public AttendanceRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public long countConferencesByUserAndStatusAndTime(User user, ApplyStatus applyStatus, boolean isConferenceUpcoming) {
        MatchOperation userMatch = Aggregation.match(Criteria.where("user.$id").is(user.getId()));
        MatchOperation statusMatch = Aggregation.match(Criteria.where("status").is(applyStatus));

        LookupOperation lookupConference = LookupOperation.newLookup()
                .from("conference")
                .localField("conference.$id")
                .foreignField("_id")
                .as("conference");
        MatchOperation timeMatch = isConferenceUpcoming ? Aggregation.match(Criteria.where("conference.startDate").gte(LocalDateTime.now()))
                : Aggregation.match(Criteria.where("conference.startDate").lt(LocalDateTime.now()));

        Aggregation aggregation = Aggregation.newAggregation(userMatch, statusMatch, lookupConference, Aggregation.unwind("conference"), timeMatch, Aggregation.count().as("count"));

        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "attendance", Document.class);
        Document result = results.getUniqueMappedResult();

        return result != null ? result.getInteger("count") : 0;
    }

    @Override
    public long countAttendeesByConferenceAndStatus(Conference conference, ApplyStatus applyStatus) {
        MatchOperation conferenceMatch = Aggregation.match(Criteria.where("conference.$id").is(conference.getId()));
        MatchOperation statusMatch = Aggregation.match(Criteria.where("status").is(applyStatus));

        Aggregation aggregation = Aggregation.newAggregation(conferenceMatch, statusMatch, Aggregation.count().as("count"));

        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "attendance", Document.class);
        Document result = results.getUniqueMappedResult();

        return result != null ? result.getInteger("count") : 0;
    }
}
