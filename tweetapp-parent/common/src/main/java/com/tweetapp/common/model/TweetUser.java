package com.tweetapp.common.model;


import com.tweetapp.common.utility.DateUtility;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tweet_users")
public class TweetUser {

    @MongoId(FieldType.OBJECT_ID)
    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private String userPassword;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date createdDate = DateUtility.getCurrentDate();

}
