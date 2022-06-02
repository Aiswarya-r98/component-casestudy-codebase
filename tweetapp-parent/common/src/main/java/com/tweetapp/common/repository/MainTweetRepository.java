package com.tweetapp.common.repository;

import com.tweetapp.common.model.MainTweet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainTweetRepository extends MongoRepository<MainTweet, Integer> {

}
