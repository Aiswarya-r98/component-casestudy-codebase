package com.tweetapp.common.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Document(collection = "main_tweets")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MainTweet {


    @MongoId(FieldType.OBJECT_ID)
    private Integer tweetId;
    private String tweetString;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date createdOn;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date editedOn;
    @DBRef
    private TweetUser user;
    @DBRef
    private List<TweetUser> likedUsers;
    @DBRef
    private List<ReplyTweet> replyTweets;
}
