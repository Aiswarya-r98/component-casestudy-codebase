package com.tweetapp.api.controller;


import com.tweetapp.api.service.MainTweetApiService;
import com.tweetapp.api.service.UserApiService;
import com.tweetapp.common.service.TweetCommonService;
import com.tweetapp.common.vo.MainTweetVo;
import com.tweetapp.common.vo.PasswordResetVo;
import com.tweetapp.common.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/tweets")
public class TweetController {

    @Autowired
    private UserApiService userApiService;

    @Autowired
    private TweetCommonService tweetCommonService;

    @Autowired
    private MainTweetApiService tweetApiService;

    @Operation(summary = "User registration")
    @PostMapping(path = "/register")
    public ResponseEntity<UserVo> register(@RequestBody UserVo userVo) {
        return new ResponseEntity<>(userApiService.addUser(userVo), HttpStatus.OK);
    }

    @Operation(summary = "User profile edit")
    @PostMapping(path = "/updateuser")
    public ResponseEntity<UserVo> updateUser(@RequestBody UserVo userVo) {
        return new ResponseEntity<>(userApiService.updateUser(userVo), HttpStatus.OK);
    }


    @Operation(summary = "Reset Password")
    @PutMapping(path = "/forgot")
    public ResponseEntity<UserVo> forgotPassword(@RequestBody PasswordResetVo passwordResetVo) {
        return new ResponseEntity<>(userApiService.forgotPassword(passwordResetVo.getUserName(), passwordResetVo.getEmail(), passwordResetVo.getUserPassword()), HttpStatus.OK);
    }

    @Operation(summary = "Search user by username")
    @Parameters({
            @Parameter(name = "userName", description = "UserName", schema = @Schema(implementation = String.class)),
    })
    @GetMapping(path = "/user/search/{username}")
    public ResponseEntity<UserVo> searchByUserName(@PathVariable("username") String userName) {
        return new ResponseEntity<>(userApiService.getUserByUserName(userName), HttpStatus.OK);
    }

    @Operation(summary = "Get all users")
    @GetMapping(path = "/users")
    public ResponseEntity<List<UserVo>> getAllUsers() {
        return new ResponseEntity<>(userApiService.getUsera(), HttpStatus.OK);
    }

    @Operation(summary = "Get all tweets")
    @GetMapping(path = "/all")
    public ResponseEntity<List<MainTweetVo>> allTweets() {
        return new ResponseEntity<>(tweetApiService.viewAllTweets(), HttpStatus.OK);
    }

    @Operation(summary = "Get tweets by username")
    @Parameters({
            @Parameter(name = "userName", description = "UserName", schema = @Schema(implementation = String.class))
    })
    @GetMapping(path = "/{username}")
    public ResponseEntity<List<MainTweetVo>> searchTweetsByUserName(@PathVariable("username") String userName) {
        return new ResponseEntity<>(tweetApiService.getTweetsByUserName(userName), HttpStatus.OK);
    }

    @Operation(summary = "Post a tweet")
    @Parameters({
            @Parameter(name = "userName", description = "UserName", schema = @Schema(implementation = String.class)),
            @Parameter(name = "tweet", description = "tweet String message", schema = @Schema(implementation = String.class))
    })
    @PostMapping(path = "/{username}/add")
    public ResponseEntity<MainTweetVo> postTweet(@PathVariable("username") String userName, @RequestParam("tweet") String tweet) throws ParseException {
        return new ResponseEntity<>(tweetApiService.postTweet(userName, tweet), HttpStatus.OK);
    }

    @Operation(summary = "Update a tweet")
    @Parameters({
            @Parameter(name = "userName", description = "UserName", schema = @Schema(implementation = String.class)),
            @Parameter(name = "id", description = "Tweet ID", schema = @Schema(implementation = Integer.class)),
            @Parameter(name = "tweet", description = "tweet String message", schema = @Schema(implementation = String.class))
    })
    @PutMapping(path = "/{username}/update/{id}")
    public ResponseEntity<MainTweetVo> updateTweet(@PathVariable("username") String userName,
                                                   @PathVariable("id") int id,
                                                   @RequestParam("tweet") String tweet) throws ParseException {
        return new ResponseEntity<>(tweetApiService.updateTweet(userName, id, tweet), HttpStatus.OK);
    }

    @Operation(summary = "Update a tweet")
    @Parameters({
            @Parameter(name = "userName", description = "UserName", schema = @Schema(implementation = String.class)),
            @Parameter(name = "id", description = "Tweet ID", schema = @Schema(implementation = Integer.class))
    })
    @DeleteMapping(path = "/{username}/delete/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable("username") String userName,
                                            @PathVariable("id") int id) {
        tweetCommonService.deleteTweetById(userName, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Like a tweet")
    @Parameters({
            @Parameter(name = "userName", description = "UserName", schema = @Schema(implementation = String.class)),
            @Parameter(name = "id", description = "Tweet ID", schema = @Schema(implementation = Integer.class))
    })
    @PutMapping(path = "/{username}/like/{id}")
    public ResponseEntity<MainTweetVo> likeTweetById(@PathVariable("username") String userName,
                                                     @PathVariable("id") int id) {

        return new ResponseEntity<>(tweetApiService.likeTweet(userName, id), HttpStatus.OK);
    }

    @Operation(summary = "Reply to a tweet")
    @Parameters({
            @Parameter(name = "userName", description = "UserName", schema = @Schema(implementation = String.class)),
            @Parameter(name = "id", description = "Tweet ID", schema = @Schema(implementation = Integer.class)),
            @Parameter(name = "tweet", description = "Reply tweet", schema = @Schema(implementation = String.class))
    })
    @PostMapping(path = "/{username}/reply/{id}")
    public ResponseEntity<MainTweetVo> replyTweet(@PathVariable("username") String userName,
                                                  @PathVariable("id") int id,
                                                  @RequestParam("tweet") String tweet) {
        return new ResponseEntity<>(tweetApiService.replyToTweet(userName, id, tweet), HttpStatus.OK);

    }
}
