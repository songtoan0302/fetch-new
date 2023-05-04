package com.vnpt.tnvn.fetch_news.repo;

import com.vnpt.tnvn.fetch_news.model.mongo.MongoContent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoContentRepository extends MongoRepository<MongoContent, Long> {
    MongoContent findMongoContentByNewsId(long newsId);
}