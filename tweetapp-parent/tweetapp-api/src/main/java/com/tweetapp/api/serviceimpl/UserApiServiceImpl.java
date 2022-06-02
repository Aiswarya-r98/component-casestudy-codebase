package com.tweetapp.api.serviceimpl;

import com.tweetapp.api.service.UserApiService;
import com.tweetapp.common.model.TweetUser;
import com.tweetapp.common.service.UserService;
import com.tweetapp.common.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserApiServiceImpl implements UserApiService {
    @Autowired
    private UserService userService;

    @Override
    public UserVo addUser(UserVo user) {
        TweetUser userEntity = userService.addUser(user);
        return user;
    }


    @Override
    public UserVo updateUser(UserVo user) {
        TweetUser userEntity = userService.updateUser(user);
        return this.converter(userEntity);
    }

    @Override
    public UserVo getUserByUserName(String userName) {
        TweetUser userEntity = userService.getUserByUserName(userName);
        return this.converter(userEntity);
    }

    @Override
    public List<UserVo> getUsera() {
        List<TweetUser> users = userService.getUsers();
        List<UserVo> userVoList= new ArrayList<>();
        for(TweetUser user: users) {
            UserVo userVo = this.converter(user);
            userVoList.add(userVo);
        }
        return userVoList;
    }

    @Override
    public UserVo forgotPassword(String userName, String email, String password) {
        TweetUser tweetUser = userService.forgotPassword(userName, email, password);
        return this.converter(tweetUser);
    }

    private UserVo converter(TweetUser user) {
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
