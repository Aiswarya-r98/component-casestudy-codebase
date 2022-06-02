package com.tweetapp.common.service.serviceImpl;

import com.tweetapp.common.exception.InvalidInputException;
import com.tweetapp.common.exception.UserNotFoundException;
import com.tweetapp.common.model.TweetUser;
import com.tweetapp.common.model.User;
import com.tweetapp.common.repository.TweetUserRepository;
import com.tweetapp.common.repository.UserRepository;
import com.tweetapp.common.service.UserService;
import com.tweetapp.common.utility.PasswordUtility;
import com.tweetapp.common.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TweetUserRepository tweetUserRepository;


    @Override
    public TweetUser addUser(UserVo user) {
        if (this.inValidateUserName(user.getUserName()))
            throw new InvalidInputException("User with email id already exist");
        if (this.inValidateEmail(user.getEmail()))
            throw new InvalidInputException("User with username already exist");
        TweetUser userEntity = new TweetUser();
        userEntity.setUserId(this.generateTweetUserId());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setEmail(user.getEmail());
        userEntity.setUserName(user.getUserName());
        String password = PasswordUtility.decodePassword(new String(Base64.getDecoder().decode(user.getUserPassword())));
        userEntity.setUserPassword(password);
        userRepository.save(new User(this.generateUserId(), user.getUserName(), password));
        return tweetUserRepository.save(userEntity);
    }

    @Override
    public TweetUser updateUser(UserVo user) {
        log.info("{}", user.getUserName());
        TweetUser userEntity = tweetUserRepository.findByUserName(user.getUserName());

        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setEmail(user.getEmail());
        return tweetUserRepository.save(userEntity);
    }


    @Override
    public List<TweetUser> getUsers() {
        return tweetUserRepository.findAll();
    }

    @Override
    public TweetUser getUserByUserName(String userName) {
        return tweetUserRepository.findByUserName(userName);
    }

    @Override
    public TweetUser getUserByEmail(String email) {
        return tweetUserRepository.findByEmail(email);
    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById((int) userId);
    }

    @Override
    public TweetUser forgotPassword(String userName, String email, String password) {

        TweetUser user = this.getUserByUserName(userName);
        if (user == null) {
            throw new UserNotFoundException();
        }

        if (!user.getEmail().equalsIgnoreCase(email)) {
            throw new UserNotFoundException();
        }
        log.info("{}", password);
        user.setUserPassword(PasswordUtility.encodePassword(password));
        User loginUser = userRepository.findByUserName(userName);
        loginUser.setUserPassword(PasswordUtility.encodePassword(password));
        userRepository.save(loginUser);
        return tweetUserRepository.save(user);
    }

    private Integer generateUserId() {
        List<User> users = userRepository.findAll();
        if (users != null && users.isEmpty())
            return 1;
        return users.
                stream().map(User::getUserId).max(Integer::compareTo).get() + 1;
    }

    private Integer generateTweetUserId() {
        List<TweetUser> users = tweetUserRepository.findAll();
        if (users != null && users.isEmpty())
            return 1;
        return users.
                stream().map(TweetUser::getUserId).max(Integer::compareTo).get() + 1;
    }

    private boolean inValidateUserName(String userName) {
        return this.getUserByUserName(userName) != null;
    }

    private boolean inValidateEmail(String email) {
        return this.getUserByEmail(email) != null;
    }
}
