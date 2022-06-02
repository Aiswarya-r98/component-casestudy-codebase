package com.tweetapp.api.serviceimpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tweetapp.common.model.TweetUser;
import com.tweetapp.common.service.UserService;
import com.tweetapp.common.vo.UserVo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserApiServiceImpl.class})
@ExtendWith(SpringExtension.class)
class UserApiServiceImplTest {
    @Autowired
    private UserApiServiceImpl userApiServiceImpl;

    @MockBean
    private UserService userService;

    /**
     * Method under test: {@link UserApiServiceImpl#addUser(UserVo)}
     */
    @Test
    void testAddUser() {
        // Arrange
        TweetUser tweetUser = new TweetUser();
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser.setCreatedDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser.setEmail("jane.doe@example.org");
        tweetUser.setFirstName("Jane");
        tweetUser.setLastName("Doe");
        tweetUser.setUserId(123);
        tweetUser.setUserName("janedoe");
        tweetUser.setUserPassword("iloveyou");
        when(this.userService.addUser((UserVo) any())).thenReturn(tweetUser);
        UserVo userVo = new UserVo();

        // Act and Assert
        assertSame(userVo, this.userApiServiceImpl.addUser(userVo));
        verify(this.userService).addUser((UserVo) any());
    }

    /**
     * Method under test: {@link UserApiServiceImpl#updateUser(UserVo)}
     */
    @Test
    void testUpdateUser() {
        // Arrange
        TweetUser tweetUser = new TweetUser();
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser.setCreatedDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser.setEmail("jane.doe@example.org");
        tweetUser.setFirstName("Jane");
        tweetUser.setLastName("Doe");
        tweetUser.setUserId(123);
        tweetUser.setUserName("janedoe");
        tweetUser.setUserPassword("iloveyou");
        when(this.userService.updateUser((UserVo) any())).thenReturn(tweetUser);

        // Act
        UserVo actualUpdateUserResult = this.userApiServiceImpl.updateUser(new UserVo());

        // Assert
        assertEquals("jane.doe@example.org", actualUpdateUserResult.getEmail());
        assertEquals("iloveyou", actualUpdateUserResult.getUserPassword());
        assertEquals("janedoe", actualUpdateUserResult.getUserName());
        assertEquals(123, actualUpdateUserResult.getUserId().intValue());
        assertEquals("Doe", actualUpdateUserResult.getLastName());
        assertEquals("Jane", actualUpdateUserResult.getFirstName());
        verify(this.userService).updateUser((UserVo) any());
    }

    /**
     * Method under test: {@link UserApiServiceImpl#getUserByUserName(String)}
     */
    @Test
    void testGetUserByUserName() {
        // Arrange
        TweetUser tweetUser = new TweetUser();
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser.setCreatedDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser.setEmail("jane.doe@example.org");
        tweetUser.setFirstName("Jane");
        tweetUser.setLastName("Doe");
        tweetUser.setUserId(123);
        tweetUser.setUserName("janedoe");
        tweetUser.setUserPassword("iloveyou");
        when(this.userService.getUserByUserName((String) any())).thenReturn(tweetUser);

        // Act
        UserVo actualUserByUserName = this.userApiServiceImpl.getUserByUserName("janedoe");

        // Assert
        assertEquals("jane.doe@example.org", actualUserByUserName.getEmail());
        assertEquals("iloveyou", actualUserByUserName.getUserPassword());
        assertEquals("janedoe", actualUserByUserName.getUserName());
        assertEquals(123, actualUserByUserName.getUserId().intValue());
        assertEquals("Doe", actualUserByUserName.getLastName());
        assertEquals("Jane", actualUserByUserName.getFirstName());
        verify(this.userService).getUserByUserName((String) any());
    }

    /**
     * Method under test: {@link UserApiServiceImpl#getUsera()}
     */
    @Test
    void testGetUsera() {
        // Arrange
        when(this.userService.getUsers()).thenReturn(new ArrayList<>());

        // Act and Assert
        assertTrue(this.userApiServiceImpl.getUsera().isEmpty());
        verify(this.userService).getUsers();
    }

    /**
     * Method under test: {@link UserApiServiceImpl#getUsera()}
     */
    @Test
    void testGetUsera2() {
        // Arrange
        TweetUser tweetUser = new TweetUser();
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser.setCreatedDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser.setEmail("jane.doe@example.org");
        tweetUser.setFirstName("Jane");
        tweetUser.setLastName("Doe");
        tweetUser.setUserId(123);
        tweetUser.setUserName("janedoe");
        tweetUser.setUserPassword("iloveyou");

        ArrayList<TweetUser> tweetUserList = new ArrayList<>();
        tweetUserList.add(tweetUser);
        when(this.userService.getUsers()).thenReturn(tweetUserList);

        // Act and Assert
        assertEquals(1, this.userApiServiceImpl.getUsera().size());
        verify(this.userService).getUsers();
    }

    /**
     * Method under test: {@link UserApiServiceImpl#forgotPassword(String, String, String)}
     */
    @Test
    void testForgotPassword() {
        // Arrange
        TweetUser tweetUser = new TweetUser();
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser.setCreatedDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser.setEmail("jane.doe@example.org");
        tweetUser.setFirstName("Jane");
        tweetUser.setLastName("Doe");
        tweetUser.setUserId(123);
        tweetUser.setUserName("janedoe");
        tweetUser.setUserPassword("iloveyou");
        when(this.userService.forgotPassword((String) any(), (String) any(), (String) any())).thenReturn(tweetUser);

        // Act
        UserVo actualForgotPasswordResult = this.userApiServiceImpl.forgotPassword("janedoe", "jane.doe@example.org",
                "iloveyou");

        // Assert
        assertEquals("jane.doe@example.org", actualForgotPasswordResult.getEmail());
        assertEquals("iloveyou", actualForgotPasswordResult.getUserPassword());
        assertEquals("janedoe", actualForgotPasswordResult.getUserName());
        assertEquals(123, actualForgotPasswordResult.getUserId().intValue());
        assertEquals("Doe", actualForgotPasswordResult.getLastName());
        assertEquals("Jane", actualForgotPasswordResult.getFirstName());
        verify(this.userService).forgotPassword((String) any(), (String) any(), (String) any());
    }
}

