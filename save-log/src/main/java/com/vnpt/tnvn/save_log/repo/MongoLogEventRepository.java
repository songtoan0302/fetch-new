package com.vnpt.tnvn.save_log.repo;

import com.vnpt.tnvn.save_log.model.MongoLogEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoLogEventRepository extends MongoRepository<MongoLogEvent, Long> {
}
