package com.tweetapp.api.security.service.impl;

import com.tweetapp.api.security.service.UserApiSecurityService;
import com.tweetapp.common.exception.UserNotFoundException;
import com.tweetapp.common.model.TweetUser;
import com.tweetapp.common.model.User;
import com.tweetapp.common.repository.TweetUserRepository;
import com.tweetapp.common.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserApiSecurityServiceImpl implements UserApiSecurityService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) {
        User user = this.getUserByUserName(userName);
        if (user == null) throw new UserNotFoundException("Couldn't find user with username " + userName);
        return user;
    }

    @Override
    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}
