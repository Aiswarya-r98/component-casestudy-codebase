package com.tweetapp.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserVo {

    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private String userName;
    private String userPassword;
}
