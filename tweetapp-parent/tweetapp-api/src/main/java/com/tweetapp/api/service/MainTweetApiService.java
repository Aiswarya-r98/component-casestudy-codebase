package com.tweetapp.api.service;

import com.tweetapp.common.vo.MainTweetVo;

import java.text.ParseException;
import java.util.List;

public interface MainTweetApiService {
    MainTweetVo postTweet(String userName, String newTweet) throws ParseException;

    MainTweetVo updateTweet(String userName, int id, String newTweet) throws ParseException;

    MainTweetVo replyToTweet(String userName, int id, String newTweet);

    MainTweetVo likeTweet(String userName, int id);

    List<MainTweetVo> getTweetsByUserName(String userName);

    List<MainTweetVo> viewAllTweets();
}
