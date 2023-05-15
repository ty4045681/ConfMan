package com.finale.ConferenceManagement.repository;

import com.finale.ConferenceManagement.interfaces.PaperRepositoryCustom;
import com.finale.ConferenceManagement.model.*;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.time.LocalDateTime;
import java.util.List;

public class PaperRepositoryImpl implements PaperRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    public PaperRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public long countByAuthorAndStatusAndConferenceTime(User author, ApplyStatus status, boolean isConferenceUpcoming) {
        MatchOperation userMatch = Aggregation.match(Criteria.where("author.$id").is(author.getId()));
        MatchOperation statusMatch = Aggregation.match(Criteria.where("status").is(status));

        LookupOperation lookupConference = LookupOperation.newLookup()
                .from("conference")
                .localField("conference.$id")
                .foreignField("_id")
                .as("conference");
        MatchOperation timeMatch = isConferenceUpcoming ? Aggregation.match(Criteria.where("conference.startDate").gte(LocalDateTime.now()))
                : Aggregation.match(Criteria.where("conference.startDate").lt(LocalDateTime.now()));

        Aggregation aggregation = Aggregation.newAggregation(userMatch, statusMatch, lookupConference, Aggregation.unwind("conference"), timeMatch, Aggregation.count().as("count"));

        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "paper", Document.class);
        Document result = results.getUniqueMappedResult();

        return result != null ? result.getInteger("count") : 0;
    }

    @Override
    public long countByConference(Conference conference) {
        MatchOperation conferenceMatch = Aggregation.match(Criteria.where("conference.$id").is(conference.getId()));

        Aggregation aggregation = Aggregation.newAggregation(conferenceMatch, Aggregation.count().as("count"));

        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "paper", Document.class);
        Document result = results.getUniqueMappedResult();

        return result != null ? result.getInteger("count") : 0;
    }

    @Override
    public List<Paper> findPapersByConference(Conference conference) {
        MatchOperation conferenceMatch = Aggregation.match(Criteria.where("conference.$id").is(conference.getId()));

        Aggregation aggregation = Aggregation.newAggregation(conferenceMatch);

        AggregationResults<Paper> results = mongoTemplate.aggregate(aggregation, "paper", Paper.class);

        return results.getMappedResults();
    }

}
