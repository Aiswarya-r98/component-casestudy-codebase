package com.tweetapp.api.serviceimpl;

import com.tweetapp.api.middleware.TweetKafkaProducer;
import com.tweetapp.api.service.MainTweetApiService;
import com.tweetapp.common.exception.InvalidInputException;
import com.tweetapp.common.exception.ProcesssingException;
import com.tweetapp.common.exception.UserNotFoundException;
import com.tweetapp.common.model.MainTweet;
import com.tweetapp.common.model.ReplyTweet;
import com.tweetapp.common.model.TweetUser;
import com.tweetapp.common.repository.ReplyTweetRepository;
import com.tweetapp.common.service.TweetCommonService;
import com.tweetapp.common.service.UserService;
import com.tweetapp.common.utility.DateUtility;
import com.tweetapp.common.vo.MainTweetVo;
import com.tweetapp.common.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MainTweetApiServiceImpl implements MainTweetApiService {
    @Autowired
    private UserService userService;

    @Autowired
    private TweetCommonService tweetService;

    @Autowired
    private TweetKafkaProducer producer;

    @Autowired
    private ReplyTweetRepository replyTweetRepository;

    @Override
    public MainTweetVo postTweet(String userName, String newTweet) throws ParseException {
        TweetUser user = userService.getUserByUserName(userName);
        if (user == null)
            throw new UserNotFoundException("Please Login before posting tweets");

        try {
            MainTweet tweet = new MainTweet();
            tweet.setTweetId(tweetService.generateId());
            tweet.setTweetString(newTweet);
            tweet.setUser(user);
            tweet.setCreatedOn(DateUtility.getCurrentDate());
            tweet.setEditedOn(DateUtility.getCurrentDate());
            tweet.setLikedUsers(new ArrayList<>());
            producer.sendMessage(tweet);
            return this.tweetVoConverter(tweet);
        } catch (InvalidInputException ex) {
            throw new ProcesssingException("Cannot process tweet", ex);
        }
    }

    @Override
    public MainTweetVo updateTweet(String userName, int id, String newTweet) throws ParseException {
        TweetUser user = userService.getUserByUserName(userName);
        MainTweet tweet = null;
        if (user == null)
            throw new UserNotFoundException("Please Login before posting tweets");
        try {
            tweet = tweetService.findByTweetId(id);
            tweet.setTweetString(newTweet);
            tweet.setEditedOn(DateUtility.getCurrentDate());
            producer.sendMessage(tweet);
            return this.tweetVoConverter(tweet);

        } catch (InvalidInputException ex) {
            throw new InvalidInputException("Cannot process tweet - " + id, ex);
        }
    }

    @Override
    public MainTweetVo replyToTweet(String userName, int id, String newTweet) {
        TweetUser user = userService.getUserByUserName(userName);
        if (user == null)
            throw new UserNotFoundException("Please Login before posting tweets");
        try {
            MainTweet mainTweet = tweetService.findByTweetId(id);
            ReplyTweet replytweet = new ReplyTweet();
            replytweet.setTweetId(tweetService.generateReplyTweetId());
            replytweet.setTweetString(newTweet);
            replytweet.setUser(user);
            replytweet.setCreatedOn(DateUtility.getCurrentDate());
            List<ReplyTweet> replyTweets = mainTweet.getReplyTweets();
            if (replyTweets == null) {
                replyTweets = new ArrayList<>();
            }
            replyTweets.add(replytweet);
            mainTweet.setReplyTweets(replyTweets);
            producer.sendMessage(mainTweet);
            return this.tweetVoConverter(mainTweet);
        } catch (InvalidInputException ex) {
            throw new InvalidInputException("Cannot process tweet - " + id, ex);
        }
    }

    @Override
    public MainTweetVo likeTweet(String userName, int id) {
        TweetUser user = userService.getUserByUserName(userName);
        if (user == null)
            throw new UserNotFoundException("Please Login before posting tweets");
        MainTweet tweet = tweetService.findByTweetId(id);

        List<TweetUser> likedUsers = tweet.getLikedUsers();
        likedUsers.add(user);
        tweet.setLikedUsers(likedUsers);
        try {
            MainTweet tweetPersisted = tweetService.saveUpdateTweet(tweet);
            return this.tweetVoConverter(tweetPersisted);
        } catch (InvalidInputException | ParseException ex) {
            throw new InvalidInputException("Cannot process tweet - " + id, ex);
        }
    }

    @Override
    public List<MainTweetVo> getTweetsByUserName(String userName) {
        List<MainTweet> tweetPersisted = tweetService.getTweetsByUserName(userName);
        List<MainTweetVo> tweetVos = new ArrayList<>();
        for (MainTweet tweet : tweetPersisted) {
            tweetVos.add(this.tweetVoConverter(tweet));
        }
        return tweetVos;

    }

    @Override
    public List<MainTweetVo> viewAllTweets() {
        List<MainTweet> tweetPersisted = tweetService.viewAllTweets();
        List<MainTweetVo> tweetVos = new ArrayList<>();
        for (MainTweet tweet : tweetPersisted) {
            tweetVos.add(this.tweetVoConverter(tweet));
        }
        return tweetVos;
    }
    private MainTweetVo tweetVoConverter(MainTweet tweet) {
        MainTweetVo tweetVo = new MainTweetVo();
        tweetVo.setTweetId(tweet.getTweetId());
        tweetVo.setTweetString(tweet.getTweetString());
        tweetVo.setCreatedOn(tweet.getCreatedOn());
        tweetVo.setLikedUsers(tweet.getLikedUsers());
        tweetVo.setEditedOn(tweet.getEditedOn());
        tweetVo.setUser(this.userVoConverter(tweet.getUser()));
        tweetVo.setReplyTweets(tweet.getReplyTweets());
        return tweetVo;
    }

    private UserVo userVoConverter(TweetUser user) {
        UserVo userVo = new UserVo();
        userVo.setUserId(user.getUserId());
        userVo.setUserName(user.getUserName());
        userVo.setEmail(user.getEmail());
        userVo.setUserPassword(user.getUserPassword());
        userVo.setFirstName(user.getFirstName());
        userVo.setLastName(user.getLastName());
        return userVo;

    }

}
