package com.tweetapp.common.service.serviceImpl;

import com.tweetapp.common.exception.InvalidInputException;
import com.tweetapp.common.exception.UserNotFoundException;
import com.tweetapp.common.model.MainTweet;
import com.tweetapp.common.model.ReplyTweet;
import com.tweetapp.common.model.TweetUser;
import com.tweetapp.common.repository.MainTweetRepository;
import com.tweetapp.common.repository.ReplyTweetRepository;
import com.tweetapp.common.service.TweetCommonService;
import com.tweetapp.common.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TweetCommonServiceImpl implements TweetCommonService {

    @Autowired
    private MainTweetRepository mainTweetRepository;

    @Autowired
    private ReplyTweetRepository replyTweetRepository;

    @Autowired
    private UserService userService;

    @Override
    public List<MainTweet> getTweetsByUserName(String userName) {
        return viewAllTweets().stream().filter(tweet -> tweet.getUser().getUserName().equalsIgnoreCase(userName)).collect(Collectors.toList());
    }

    @Override
    public List<MainTweet> viewAllTweets() {
        return mainTweetRepository.findAll();
    }

    @Override
    public MainTweet findByTweetId(int id) {
        return mainTweetRepository.findById(id).orElseThrow(InvalidInputException::new);
    }

    @Override
    public MainTweet saveUpdateTweet(MainTweet tweet) throws ParseException {
        List<ReplyTweet> replyTweets = tweet.getReplyTweets();
        if (replyTweets != null && !replyTweets.isEmpty()) {
            for (ReplyTweet replyTweet : replyTweets) {
                replyTweetRepository.save(replyTweet);
            }
        }
        return mainTweetRepository.save(tweet);
    }

    @Override
    public void deleteTweetById(String userName, int id) throws InvalidInputException {
        TweetUser user = userService.getUserByUserName(userName);
        if (user == null) {
            throw new UserNotFoundException("Please Login before posting tweets");
        }
        MainTweet tweet = mainTweetRepository.findById(id).orElse(null);
        if (tweet == null) {
            throw new InvalidInputException("Please select valid Tweet");
        }
        List<ReplyTweet> replyTweets = tweet.getReplyTweets();
        if (replyTweets != null && !replyTweets.isEmpty()) {
            for (ReplyTweet replyTweet : replyTweets) {
                replyTweetRepository.deleteById(replyTweet.getTweetId());
            }
        }
        mainTweetRepository.delete(tweet);
    }

    @Override
    public int generateId() {
        int tweetId = mainTweetRepository.findAll().stream().map(MainTweet::getTweetId).max(Integer::compareTo).orElse(0);
        return tweetId + 1;
    }

    @Override
    public int generateReplyTweetId() {
        int tweetId = replyTweetRepository.findAll().stream().map(ReplyTweet::getTweetId).max(Integer::compareTo).orElse(0);
        return tweetId + 1;
    }
}
