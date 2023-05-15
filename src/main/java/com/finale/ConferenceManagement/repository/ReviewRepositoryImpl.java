package com.finale.ConferenceManagement.repository;

import com.finale.ConferenceManagement.interfaces.ReviewRepositoryCustom;
import com.finale.ConferenceManagement.model.*;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
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
    public List<User> findJudgesByConference(Conference conference) {
        MatchOperation conferenceMatch = Aggregation.match(Criteria.where("conference.$id").is(conference.getId()));

        GroupOperation groupByJudge = Aggregation.group("judge.$id").first("judge").as("judge");
        ProjectionOperation projectJudge = Aggregation.project("judge").andExclude("_id");

        Aggregation aggregation = Aggregation.newAggregation(conferenceMatch, groupByJudge, projectJudge);

        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "review", Document.class);

        return results.getMappedResults().stream()
                .map(document -> mongoTemplate.getConverter().read(User.class, document))
                .collect(Collectors.toList());
    }

    @Override
    public List<Review> findReviewsByConference(Conference conference) {
        MatchOperation conferenceMatch = Aggregation.match(Criteria.where("conference.$id").is(conference.getId()));

        Aggregation aggregation = Aggregation.newAggregation(conferenceMatch);

        AggregationResults<Review> results = mongoTemplate.aggregate(aggregation, "review", Review.class);

        return results.getMappedResults();
    }

    @Override
    public List<Review> findReviewsByJudge(User judge) {
        MatchOperation judgeMatch = Aggregation.match(Criteria.where("judge.$id").is(judge.getId()));

        Aggregation aggregation = Aggregation.newAggregation(judgeMatch);

        AggregationResults<Review> results = mongoTemplate.aggregate(aggregation, "review", Review.class);

        return results.getMappedResults();
    }

    @Override
    public User findJudgeByReview(Review review) {
        UUID judgeId = review.getJudge().getId();
        Query query = Query.query(Criteria.where("_id").is(judgeId));
        return mongoTemplate.findOne(query, User.class);
    }

    @Override
    public Paper findPaperByReview(Review review) {
        UUID paperId = review.getPaper().getId();
        Query query = Query.query(Criteria.where("_id").is(paperId));
        return mongoTemplate.findOne(query, Paper.class);
    }

    @Override
    public long countPapersByJudgeAndStatus(User judge, ApplyStatus status) {
        MatchOperation judgeMatch = Aggregation.match(Criteria.where("judge.$id").is(judge.getId()));
        MatchOperation statusMatch = Aggregation.match(Criteria.where("applyStatus").is(status));

        GroupOperation groupByPaper = Aggregation.group("paper.$id");
        GroupOperation countDistinctPapers = Aggregation.group().count().as("count");

        Aggregation aggregation = Aggregation.newAggregation(judgeMatch, statusMatch, groupByPaper, countDistinctPapers);

        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "review", Document.class);
        Document result = results.getUniqueMappedResult();

        return result != null ? result.getInteger("count") : 0;
    }
}
