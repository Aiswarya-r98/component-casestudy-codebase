package com.tweetapp.api.security.service;

import com.tweetapp.common.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserApiSecurityService extends UserDetailsService {


    User getUserByUserName(String userName);

}
