package com.finale.ConferenceManagement.repository;

import com.finale.ConferenceManagement.interfaces.ConferenceRepositoryCustom;
import com.finale.ConferenceManagement.model.Conference;
import com.finale.ConferenceManagement.model.User;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class ConferenceRepositoryImpl implements ConferenceRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    @Override
    public long countConferencesByOrganizer(User organizer) {
        MatchOperation userMatch = Aggregation.match(Criteria.where("organizer.$id").is(organizer.getId()));

        Aggregation aggregation = Aggregation.newAggregation(userMatch, Aggregation.count().as("count"));

        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "conference", Document.class);
        Document result = results.getUniqueMappedResult();

        return result != null ? result.getInteger("count") : -1;
    }

    @Override
    public List<Conference> findConferencesByOrganizer(User organizer) {
        MatchOperation userMatch = Aggregation.match(Criteria.where("organizer.$id").is(organizer.getId()));

        Aggregation aggregation = Aggregation.newAggregation(userMatch);

        AggregationResults<Conference> results = mongoTemplate.aggregate(aggregation, "conference", Conference.class);

        return results.getMappedResults();
    }
}
