package com.tweetapp.common.vo;

import com.tweetapp.common.model.ReplyTweet;
import com.tweetapp.common.model.TweetUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TweetVo {

    private Integer tweetId;
    private String tweetString;
    private Date createdOn;
    private Date editedOn;
    private UserVo user;
    private boolean isReply = false;
    private Integer mainTweetId = 0;
    private List<TweetUser> likedUsers;
    private List<ReplyTweet> replyTweets;
}
