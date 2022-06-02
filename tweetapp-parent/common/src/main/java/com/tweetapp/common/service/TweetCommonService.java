package com.tweetapp.common.service;

import com.tweetapp.common.model.MainTweet;

import java.text.ParseException;
import java.util.List;


public interface TweetCommonService {
    List<MainTweet> getTweetsByUserName(String userName);

    List<MainTweet> viewAllTweets();

    MainTweet findByTweetId(int id);

    MainTweet saveUpdateTweet(MainTweet tweet) throws ParseException;

    void deleteTweetById(String userName, int id);

    int generateId();

    int generateReplyTweetId();

}
