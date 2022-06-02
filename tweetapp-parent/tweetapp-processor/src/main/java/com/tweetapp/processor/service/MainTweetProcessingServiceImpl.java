package com.tweetapp.processor.service;

import com.tweetapp.common.exception.ProcesssingException;
import com.tweetapp.common.model.MainTweet;
import com.tweetapp.common.service.TweetCommonService;
import com.tweetapp.common.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
@Slf4j
public class MainTweetProcessingServiceImpl implements MainTweetProcessingService {

    @Autowired
    private TweetCommonService tweetService;

    @Autowired
    private UserService userService;

    @Override
    public MainTweet processTweet(MainTweet tweet) {
        try {
            log.debug("saving tweet {}", tweet);
            log.debug("saving tweet {}", tweet.getTweetId());
            return tweetService.saveUpdateTweet(tweet);
        } catch (ParseException e) {
            throw new ProcesssingException("Cannot Process MainTweet - " + tweet.getTweetId(), e);
        }
    }
}
