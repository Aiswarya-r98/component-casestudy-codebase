package com.tweetapp.common.repository;

import com.tweetapp.common.model.ReplyTweet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyTweetRepository extends MongoRepository<ReplyTweet, Integer> {

}
