package com.tweetapp.processor.service;

import com.tweetapp.common.model.MainTweet;
import com.tweetapp.common.model.TweetUser;
import com.tweetapp.common.service.TweetCommonService;
import com.tweetapp.common.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class MainTweetProcessingServiceImplTest {
    @InjectMocks
    private MainTweetProcessingServiceImpl mainTweetProcessingServiceImpl;

    @Mock
    private TweetCommonService tweetCommonService;

    @Mock
    private UserService userService;

    /**
     * Method under test: {@link MainTweetProcessingServiceImpl#processTweet(MainTweet)}
     */
    @Test
    void testProcessTweet() throws ParseException {
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

        MainTweet mainTweet = new MainTweet();
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setCreatedOn(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setEditedOn(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        mainTweet.setLikedUsers(new ArrayList<>());
        mainTweet.setReplyTweets(new ArrayList<>());
        mainTweet.setTweetId(123);
        mainTweet.setTweetString("Tweet String");
        mainTweet.setUser(tweetUser);
        when(this.tweetCommonService.saveUpdateTweet((MainTweet) any())).thenReturn(mainTweet);

        TweetUser tweetUser1 = new TweetUser();
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser1.setCreatedDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser1.setEmail("jane.doe@example.org");
        tweetUser1.setFirstName("Jane");
        tweetUser1.setLastName("Doe");
        tweetUser1.setUserId(123);
        tweetUser1.setUserName("janedoe");
        tweetUser1.setUserPassword("iloveyou");

        MainTweet mainTweet1 = new MainTweet();
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet1.setCreatedOn(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet1.setEditedOn(Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant()));
        mainTweet1.setLikedUsers(new ArrayList<>());
        mainTweet1.setReplyTweets(new ArrayList<>());
        mainTweet1.setTweetId(123);
        mainTweet1.setTweetString("Tweet String");
        mainTweet1.setUser(tweetUser1);

        // Act and Assert
        assertSame(mainTweet, this.mainTweetProcessingServiceImpl.processTweet(mainTweet1));
        verify(this.tweetCommonService).saveUpdateTweet((MainTweet) any());
    }
}

