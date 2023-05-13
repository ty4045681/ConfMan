package com.finale.ConferenceManagement.repository;

import com.finale.ConferenceManagement.interfaces.ReviewRepositoryCustom;
import com.finale.ConferenceManagement.model.Conference;
import com.finale.ConferenceManagement.model.User;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {
    private final MongoTemplate mongoTemplate;

    @Override
    public long countJudgesByConference(Conference conference) {
        MatchOperation conferenceMatch = Aggregation.match(Criteria.where("conference.$id").is(conference.getId()));

        GroupOperation groupByJudge = Aggregation.group("judge.$id");
        GroupOperation countDistinctJudges = Aggregation.group().count().as("count");

        Aggregation aggregation = Aggregation.newAggregation(conferenceMatch, groupByJudge, countDistinctJudges);

        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "review", Document.class);
        Document result = results.getUniqueMappedResult();

        return result != null ? result.getInteger("count") : 0;
    }

    @Override
    public Set<User> findJudgesByConference(Conference conference) {
        MatchOperation conferenceMatch = Aggregation.match(Criteria.where("conference.$id").is(conference.getId()));

        GroupOperation groupByJudge = Aggregation.group("judge");

        LookupOperation lookupUser = LookupOperation.newLookup()
                .from("user")
                .localField("judge.$id")
                .foreignField("_id")
                .as("judgeInfo");

        Aggregation aggregation = Aggregation.newAggregation(conferenceMatch, groupByJudge, lookupUser);

        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "review", Document.class);
        List<Document> judgeDocuments = results.getMappedResults();

        // Extract judgeInfo from the documents
        Set<User> judges = judgeDocuments.stream()
                .map(doc -> ((List<Document>) doc.get("judgeInfo")).get(0))
                .map(judgeDoc -> mongoTemplate.getConverter().read(User.class, judgeDoc))
                .collect(Collectors.toSet());

        return judges;
    }
}
