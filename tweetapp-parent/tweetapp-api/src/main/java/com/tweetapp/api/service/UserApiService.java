package com.tweetapp.api.service;

import com.tweetapp.common.vo.UserVo;

import java.util.List;

public interface UserApiService {
    UserVo addUser(UserVo user);

    UserVo updateUser(UserVo user);

    UserVo forgotPassword(String userName, String email, String password);

    UserVo getUserByUserName(String userName);

    List<UserVo> getUsera();

}
