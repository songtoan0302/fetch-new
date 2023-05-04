package com.vnpt.tnvn.refresh_token;

import com.vnpt.tnvn.refresh_token.model.Token;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TokenRepository extends MongoRepository<Token, ObjectId> {
    List<Token> findTokensByRefreshTokenNotNull();
}