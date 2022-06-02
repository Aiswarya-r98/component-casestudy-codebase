package com.tweetapp.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReplyTweetVo {

    private Integer tweetId;
    private String tweetString;
    private Date createdOn;
    private UserVo user;
}
