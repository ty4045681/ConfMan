package com.finale.ConferenceManagement.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoTransactionManagerConfig {
    private final MongoTemplate mongoTemplate;

    public MongoTransactionManagerConfig(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Bean
    MongoTransactionManager transactionManager() {
        return new MongoTransactionManager(mongoTemplate.getMongoDatabaseFactory());
    }
}
