package com.tweetapp.common.repository;

import com.tweetapp.common.model.TweetUser;
import com.tweetapp.common.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetUserRepository extends MongoRepository<TweetUser, Integer> {

    @Query("{'userName' : ?0}")
    TweetUser findByUserName(String userName);
    @Query("{'email' : ?0}")
    TweetUser findByEmail(String email);
}
