package com.tweetapp.common.service;

import com.tweetapp.common.model.TweetUser;
import com.tweetapp.common.model.User;
import com.tweetapp.common.vo.UserVo;

import java.util.List;

public interface UserService {

    TweetUser addUser(UserVo user);
    TweetUser updateUser(UserVo user);

    List<TweetUser> getUsers();

    TweetUser getUserByUserName(String userName);

    TweetUser getUserByEmail(String email);

    void deleteUser(long userId);

    TweetUser forgotPassword(String userName, String email, String password);
}
