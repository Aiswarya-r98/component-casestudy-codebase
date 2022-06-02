package com.tweetapp.api.serviceimpl;

import com.tweetapp.api.middleware.TweetKafkaProducer;
import com.tweetapp.common.exception.InvalidInputException;
import com.tweetapp.common.exception.ProcesssingException;
import com.tweetapp.common.exception.UserNotFoundException;
import com.tweetapp.common.model.MainTweet;
import com.tweetapp.common.model.ReplyTweet;
import com.tweetapp.common.model.TweetUser;
import com.tweetapp.common.repository.ReplyTweetRepository;
import com.tweetapp.common.service.TweetCommonService;
import com.tweetapp.common.service.UserService;
import com.tweetapp.common.vo.MainTweetVo;
import com.tweetapp.common.vo.UserVo;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class MainTweetApiServiceImplTest {
    @InjectMocks
    private MainTweetApiServiceImpl mainTweetApiServiceImpl;

    @Mock
    private ReplyTweetRepository replyTweetRepository;

    @Mock
    private TweetCommonService tweetCommonService;

    @Mock
    private TweetKafkaProducer tweetKafkaProducer;

    @Mock
    private UserService userService;

    /**
     * Method under test: {@link MainTweetApiServiceImpl#postTweet(String, String)}
     */
    @Test
    void testPostTweet() throws ParseException {
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
        ProducerRecord<String, MainTweet> producerRecord = new ProducerRecord<>("Topic", new MainTweet());

        when(this.tweetKafkaProducer.sendMessage((MainTweet) any())).thenReturn(new AsyncResult<>(
                new SendResult<>(producerRecord, new RecordMetadata(new TopicPartition("Topic", 1), 1L, 1, 10L, 3, 3))));
        when(this.tweetCommonService.generateId()).thenReturn(123);

        // Act
        MainTweetVo actualPostTweetResult = this.mainTweetApiServiceImpl.postTweet("janedoe", "New Tweet");

        // Assert
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals("2020-09-23", simpleDateFormat.format(actualPostTweetResult.getCreatedOn()));
        assertEquals("New Tweet", actualPostTweetResult.getTweetString());
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals("2020-09-23", simpleDateFormat1.format(actualPostTweetResult.getEditedOn()));
        assertNull(actualPostTweetResult.getReplyTweets());
        assertEquals(123, actualPostTweetResult.getTweetId().intValue());
        assertTrue(actualPostTweetResult.getLikedUsers().isEmpty());
        UserVo user = actualPostTweetResult.getUser();
        assertEquals("Jane", user.getFirstName());
        assertEquals("jane.doe@example.org", user.getEmail());
        assertEquals("janedoe", user.getUserName());
        assertEquals("iloveyou", user.getUserPassword());
        assertEquals("Doe", user.getLastName());
        assertEquals(123, user.getUserId().intValue());
        verify(this.userService).getUserByUserName((String) any());
        verify(this.tweetKafkaProducer).sendMessage((MainTweet) any());
        verify(this.tweetCommonService).generateId();
    }

    /**
     * Method under test: {@link MainTweetApiServiceImpl#postTweet(String, String)}
     */
    @Test
    void testPostTweet2() throws ParseException {
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
        ProducerRecord<String, MainTweet> producerRecord = new ProducerRecord<>("Topic", new MainTweet());

        when(this.tweetKafkaProducer.sendMessage((MainTweet) any())).thenReturn(new AsyncResult<>(
                new SendResult<>(producerRecord, new RecordMetadata(new TopicPartition("Topic", 1), 1L, 1, 10L, 3, 3))));
        when(this.tweetCommonService.generateId()).thenThrow(new UserNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> this.mainTweetApiServiceImpl.postTweet("janedoe", "New Tweet"));
        verify(this.userService).getUserByUserName((String) any());
        verify(this.tweetCommonService).generateId();
    }

    /**
     * Method under test: {@link MainTweetApiServiceImpl#postTweet(String, String)}
     */
    @Test
    void testPostTweet3() throws ParseException {
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
        ProducerRecord<String, MainTweet> producerRecord = new ProducerRecord<>("Topic", new MainTweet());

        when(this.tweetKafkaProducer.sendMessage((MainTweet) any())).thenReturn(new AsyncResult<>(
                new SendResult<>(producerRecord, new RecordMetadata(new TopicPartition("Topic", 1), 1L, 1, 10L, 3, 3))));
        when(this.tweetCommonService.generateId()).thenThrow(new InvalidInputException("An error occurred"));

        // Act and Assert
        assertThrows(ProcesssingException.class, () -> this.mainTweetApiServiceImpl.postTweet("janedoe", "New Tweet"));
        verify(this.userService).getUserByUserName((String) any());
        verify(this.tweetCommonService).generateId();
    }

    /**
     * Method under test: {@link MainTweetApiServiceImpl#updateTweet(String, int, String)}
     */
    @Test
    void testUpdateTweet() throws ParseException {
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
        ProducerRecord<String, MainTweet> producerRecord = new ProducerRecord<>("Topic", new MainTweet());

        when(this.tweetKafkaProducer.sendMessage((MainTweet) any())).thenReturn(new AsyncResult<>(
                new SendResult<>(producerRecord, new RecordMetadata(new TopicPartition("Topic", 1), 1L, 1, 10L, 3, 3))));

        TweetUser tweetUser1 = new TweetUser();
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser1.setCreatedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser1.setEmail("jane.doe@example.org");
        tweetUser1.setFirstName("Jane");
        tweetUser1.setLastName("Doe");
        tweetUser1.setUserId(123);
        tweetUser1.setUserName("janedoe");
        tweetUser1.setUserPassword("iloveyou");

        MainTweet mainTweet = new MainTweet();
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        Date fromResult = Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant());
        mainTweet.setCreatedOn(fromResult);
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setEditedOn(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        mainTweet.setLikedUsers(new ArrayList<>());
        mainTweet.setReplyTweets(new ArrayList<>());
        mainTweet.setTweetId(123);
        mainTweet.setTweetString("Tweet String");
        mainTweet.setUser(tweetUser1);
        when(this.tweetCommonService.findByTweetId(anyInt())).thenReturn(mainTweet);

        // Act
        MainTweetVo actualUpdateTweetResult = this.mainTweetApiServiceImpl.updateTweet("janedoe", 1, "New Tweet");

        // Assert
        assertSame(fromResult, actualUpdateTweetResult.getCreatedOn());
        assertEquals("New Tweet", actualUpdateTweetResult.getTweetString());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals("2020-09-23", simpleDateFormat.format(actualUpdateTweetResult.getEditedOn()));
        assertTrue(actualUpdateTweetResult.getReplyTweets().isEmpty());
        assertEquals(123, actualUpdateTweetResult.getTweetId().intValue());
        assertTrue(actualUpdateTweetResult.getLikedUsers().isEmpty());
        UserVo user = actualUpdateTweetResult.getUser();
        assertEquals("Jane", user.getFirstName());
        assertEquals("jane.doe@example.org", user.getEmail());
        assertEquals("janedoe", user.getUserName());
        assertEquals("iloveyou", user.getUserPassword());
        assertEquals("Doe", user.getLastName());
        assertEquals(123, user.getUserId().intValue());
        verify(this.userService).getUserByUserName((String) any());
        verify(this.tweetKafkaProducer).sendMessage((MainTweet) any());
        verify(this.tweetCommonService).findByTweetId(anyInt());
    }

    /**
     * Method under test: {@link MainTweetApiServiceImpl#updateTweet(String, int, String)}
     */
    @Test
    void testUpdateTweet2() throws ParseException {
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
        when(this.tweetKafkaProducer.sendMessage((MainTweet) any()))
                .thenThrow(new UserNotFoundException("An error occurred"));

        TweetUser tweetUser1 = new TweetUser();
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser1.setCreatedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser1.setEmail("jane.doe@example.org");
        tweetUser1.setFirstName("Jane");
        tweetUser1.setLastName("Doe");
        tweetUser1.setUserId(123);
        tweetUser1.setUserName("janedoe");
        tweetUser1.setUserPassword("iloveyou");

        MainTweet mainTweet = new MainTweet();
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setCreatedOn(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setEditedOn(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        mainTweet.setLikedUsers(new ArrayList<>());
        mainTweet.setReplyTweets(new ArrayList<>());
        mainTweet.setTweetId(123);
        mainTweet.setTweetString("Tweet String");
        mainTweet.setUser(tweetUser1);
        when(this.tweetCommonService.findByTweetId(anyInt())).thenReturn(mainTweet);

        // Act and Assert
        assertThrows(UserNotFoundException.class,
                () -> this.mainTweetApiServiceImpl.updateTweet("janedoe", 1, "New Tweet"));
        verify(this.userService).getUserByUserName((String) any());
        verify(this.tweetKafkaProducer).sendMessage((MainTweet) any());
        verify(this.tweetCommonService).findByTweetId(anyInt());
    }

    /**
     * Method under test: {@link MainTweetApiServiceImpl#updateTweet(String, int, String)}
     */
    @Test
    void testUpdateTweet3() throws ParseException {
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
        when(this.tweetKafkaProducer.sendMessage((MainTweet) any()))
                .thenThrow(new InvalidInputException("An error occurred"));

        TweetUser tweetUser1 = new TweetUser();
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser1.setCreatedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser1.setEmail("jane.doe@example.org");
        tweetUser1.setFirstName("Jane");
        tweetUser1.setLastName("Doe");
        tweetUser1.setUserId(123);
        tweetUser1.setUserName("janedoe");
        tweetUser1.setUserPassword("iloveyou");

        MainTweet mainTweet = new MainTweet();
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setCreatedOn(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setEditedOn(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        mainTweet.setLikedUsers(new ArrayList<>());
        mainTweet.setReplyTweets(new ArrayList<>());
        mainTweet.setTweetId(123);
        mainTweet.setTweetString("Tweet String");
        mainTweet.setUser(tweetUser1);
        when(this.tweetCommonService.findByTweetId(anyInt())).thenReturn(mainTweet);

        // Act and Assert
        assertThrows(InvalidInputException.class,
                () -> this.mainTweetApiServiceImpl.updateTweet("janedoe", 1, "New Tweet"));
        verify(this.userService).getUserByUserName((String) any());
        verify(this.tweetKafkaProducer).sendMessage((MainTweet) any());
        verify(this.tweetCommonService).findByTweetId(anyInt());
    }

    /**
     * Method under test: {@link MainTweetApiServiceImpl#updateTweet(String, int, String)}
     */
    @Test
    void testUpdateTweet4() throws ParseException {
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
        ProducerRecord<String, MainTweet> producerRecord = new ProducerRecord<>("Topic", new MainTweet());

        when(this.tweetKafkaProducer.sendMessage((MainTweet) any())).thenReturn(new AsyncResult<>(
                new SendResult<>(producerRecord, new RecordMetadata(new TopicPartition("Topic", 1), 1L, 1, 10L, 3, 3))));

        TweetUser tweetUser1 = new TweetUser();
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser1.setCreatedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser1.setEmail("jane.doe@example.org");
        tweetUser1.setFirstName("Jane");
        tweetUser1.setLastName("Doe");
        tweetUser1.setUserId(123);
        tweetUser1.setUserName("janedoe");
        tweetUser1.setUserPassword("iloveyou");

        TweetUser tweetUser2 = new TweetUser();
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser2.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser2.setEmail("jane.doe@example.org");
        tweetUser2.setFirstName("Jane");
        tweetUser2.setLastName("Doe");
        tweetUser2.setUserId(123);
        tweetUser2.setUserName("janedoe");
        tweetUser2.setUserPassword("iloveyou");
        MainTweet mainTweet = mock(MainTweet.class);
        when(mainTweet.getReplyTweets()).thenThrow(new UserNotFoundException("An error occurred"));
        when(mainTweet.getUser()).thenReturn(tweetUser2);
        when(mainTweet.getTweetId()).thenReturn(123);
        when(mainTweet.getTweetString()).thenReturn("Tweet String");
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(mainTweet.getCreatedOn()).thenReturn(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(mainTweet.getEditedOn()).thenReturn(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        when(mainTweet.getLikedUsers()).thenReturn(new ArrayList<>());
        doNothing().when(mainTweet).setCreatedOn((Date) any());
        doNothing().when(mainTweet).setEditedOn((Date) any());
        doNothing().when(mainTweet).setLikedUsers((List<TweetUser>) any());
        doNothing().when(mainTweet).setReplyTweets((List<ReplyTweet>) any());
        doNothing().when(mainTweet).setTweetId((Integer) any());
        doNothing().when(mainTweet).setTweetString((String) any());
        doNothing().when(mainTweet).setUser((TweetUser) any());
        LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setCreatedOn(Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult6 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setEditedOn(Date.from(atStartOfDayResult6.atZone(ZoneId.of("UTC")).toInstant()));
        mainTweet.setLikedUsers(new ArrayList<>());
        mainTweet.setReplyTweets(new ArrayList<>());
        mainTweet.setTweetId(123);
        mainTweet.setTweetString("Tweet String");
        mainTweet.setUser(tweetUser1);
        when(this.tweetCommonService.findByTweetId(anyInt())).thenReturn(mainTweet);

        // Act and Assert
        assertThrows(UserNotFoundException.class,
                () -> this.mainTweetApiServiceImpl.updateTweet("janedoe", 1, "New Tweet"));
        verify(this.userService).getUserByUserName((String) any());
        verify(this.tweetKafkaProducer).sendMessage((MainTweet) any());
        verify(this.tweetCommonService).findByTweetId(anyInt());
        verify(mainTweet).getUser();
        verify(mainTweet).getTweetId();
        verify(mainTweet).getTweetString();
        verify(mainTweet).getCreatedOn();
        verify(mainTweet).getEditedOn();
        verify(mainTweet).getLikedUsers();
        verify(mainTweet).getReplyTweets();
        verify(mainTweet).setCreatedOn((Date) any());
        verify(mainTweet, atLeast(1)).setEditedOn((Date) any());
        verify(mainTweet).setLikedUsers((List<TweetUser>) any());
        verify(mainTweet).setReplyTweets((List<ReplyTweet>) any());
        verify(mainTweet).setTweetId((Integer) any());
        verify(mainTweet, atLeast(1)).setTweetString((String) any());
        verify(mainTweet).setUser((TweetUser) any());
    }

    /**
     * Method under test: {@link MainTweetApiServiceImpl#updateTweet(String, int, String)}
     */
    @Test
    void testUpdateTweet5() throws ParseException {
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
        ProducerRecord<String, MainTweet> producerRecord = new ProducerRecord<>("Topic", new MainTweet());

        when(this.tweetKafkaProducer.sendMessage((MainTweet) any())).thenReturn(new AsyncResult<>(
                new SendResult<>(producerRecord, new RecordMetadata(new TopicPartition("Topic", 1), 1L, 1, 10L, 3, 3))));

        TweetUser tweetUser1 = new TweetUser();
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser1.setCreatedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser1.setEmail("jane.doe@example.org");
        tweetUser1.setFirstName("Jane");
        tweetUser1.setLastName("Doe");
        tweetUser1.setUserId(123);
        tweetUser1.setUserName("janedoe");
        tweetUser1.setUserPassword("iloveyou");

        TweetUser tweetUser2 = new TweetUser();
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser2.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser2.setEmail("jane.doe@example.org");
        tweetUser2.setFirstName("Jane");
        tweetUser2.setLastName("Doe");
        tweetUser2.setUserId(123);
        tweetUser2.setUserName("janedoe");
        tweetUser2.setUserPassword("iloveyou");
        MainTweet mainTweet = mock(MainTweet.class);
        when(mainTweet.getReplyTweets()).thenThrow(new InvalidInputException("An error occurred"));
        when(mainTweet.getUser()).thenReturn(tweetUser2);
        when(mainTweet.getTweetId()).thenReturn(123);
        when(mainTweet.getTweetString()).thenReturn("Tweet String");
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(mainTweet.getCreatedOn()).thenReturn(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(mainTweet.getEditedOn()).thenReturn(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        when(mainTweet.getLikedUsers()).thenReturn(new ArrayList<>());
        doNothing().when(mainTweet).setCreatedOn((Date) any());
        doNothing().when(mainTweet).setEditedOn((Date) any());
        doNothing().when(mainTweet).setLikedUsers((List<TweetUser>) any());
        doNothing().when(mainTweet).setReplyTweets((List<ReplyTweet>) any());
        doNothing().when(mainTweet).setTweetId((Integer) any());
        doNothing().when(mainTweet).setTweetString((String) any());
        doNothing().when(mainTweet).setUser((TweetUser) any());
        LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setCreatedOn(Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult6 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setEditedOn(Date.from(atStartOfDayResult6.atZone(ZoneId.of("UTC")).toInstant()));
        mainTweet.setLikedUsers(new ArrayList<>());
        mainTweet.setReplyTweets(new ArrayList<>());
        mainTweet.setTweetId(123);
        mainTweet.setTweetString("Tweet String");
        mainTweet.setUser(tweetUser1);
        when(this.tweetCommonService.findByTweetId(anyInt())).thenReturn(mainTweet);

        // Act and Assert
        assertThrows(InvalidInputException.class,
                () -> this.mainTweetApiServiceImpl.updateTweet("janedoe", 1, "New Tweet"));
        verify(this.userService).getUserByUserName((String) any());
        verify(this.tweetKafkaProducer).sendMessage((MainTweet) any());
        verify(this.tweetCommonService).findByTweetId(anyInt());
        verify(mainTweet).getUser();
        verify(mainTweet).getTweetId();
        verify(mainTweet).getTweetString();
        verify(mainTweet).getCreatedOn();
        verify(mainTweet).getEditedOn();
        verify(mainTweet).getLikedUsers();
        verify(mainTweet).getReplyTweets();
        verify(mainTweet).setCreatedOn((Date) any());
        verify(mainTweet, atLeast(1)).setEditedOn((Date) any());
        verify(mainTweet).setLikedUsers((List<TweetUser>) any());
        verify(mainTweet).setReplyTweets((List<ReplyTweet>) any());
        verify(mainTweet).setTweetId((Integer) any());
        verify(mainTweet, atLeast(1)).setTweetString((String) any());
        verify(mainTweet).setUser((TweetUser) any());
    }

    /**
     * Method under test: {@link MainTweetApiServiceImpl#replyToTweet(String, int, String)}
     */
    @Test
    void testReplyToTweet() {
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
        ProducerRecord<String, MainTweet> producerRecord = new ProducerRecord<>("Topic", new MainTweet());

        when(this.tweetKafkaProducer.sendMessage((MainTweet) any())).thenReturn(new AsyncResult<>(
                new SendResult<>(producerRecord, new RecordMetadata(new TopicPartition("Topic", 1), 1L, 1, 10L, 3, 3))));

        TweetUser tweetUser1 = new TweetUser();
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser1.setCreatedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser1.setEmail("jane.doe@example.org");
        tweetUser1.setFirstName("Jane");
        tweetUser1.setLastName("Doe");
        tweetUser1.setUserId(123);
        tweetUser1.setUserName("janedoe");
        tweetUser1.setUserPassword("iloveyou");

        MainTweet mainTweet = new MainTweet();
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        Date fromResult = Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant());
        mainTweet.setCreatedOn(fromResult);
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        Date fromResult1 = Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant());
        mainTweet.setEditedOn(fromResult1);
        mainTweet.setLikedUsers(new ArrayList<>());
        mainTweet.setReplyTweets(new ArrayList<>());
        mainTweet.setTweetId(123);
        mainTweet.setTweetString("Tweet String");
        mainTweet.setUser(tweetUser1);
        when(this.tweetCommonService.findByTweetId(anyInt())).thenReturn(mainTweet);
        when(this.tweetCommonService.generateReplyTweetId()).thenReturn(123);

        // Act
        MainTweetVo actualReplyToTweetResult = this.mainTweetApiServiceImpl.replyToTweet("janedoe", 1, "New Tweet");

        // Assert
        assertSame(fromResult, actualReplyToTweetResult.getCreatedOn());
        assertEquals("Tweet String", actualReplyToTweetResult.getTweetString());
        assertSame(fromResult1, actualReplyToTweetResult.getEditedOn());
        List<ReplyTweet> replyTweets = actualReplyToTweetResult.getReplyTweets();
        assertEquals(1, replyTweets.size());
        assertEquals(123, actualReplyToTweetResult.getTweetId().intValue());
        assertTrue(actualReplyToTweetResult.getLikedUsers().isEmpty());
        UserVo user = actualReplyToTweetResult.getUser();
        assertEquals("Doe", user.getLastName());
        assertEquals("Jane", user.getFirstName());
        assertEquals("jane.doe@example.org", user.getEmail());
        assertEquals("janedoe", user.getUserName());
        assertEquals("iloveyou", user.getUserPassword());
        assertEquals(123, user.getUserId().intValue());
        ReplyTweet getResult = replyTweets.get(0);
        assertSame(tweetUser, getResult.getUser());
        assertEquals("New Tweet", getResult.getTweetString());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals("2020-09-23", simpleDateFormat.format(getResult.getCreatedOn()));
        assertEquals(123, getResult.getTweetId().intValue());
        verify(this.userService).getUserByUserName((String) any());
        verify(this.tweetKafkaProducer).sendMessage((MainTweet) any());
        verify(this.tweetCommonService).findByTweetId(anyInt());
        verify(this.tweetCommonService).generateReplyTweetId();
    }

    /**
     * Method under test: {@link MainTweetApiServiceImpl#replyToTweet(String, int, String)}
     */
    @Test
    void testReplyToTweet2() {
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
        when(this.tweetKafkaProducer.sendMessage((MainTweet) any()))
                .thenThrow(new UserNotFoundException("An error occurred"));

        TweetUser tweetUser1 = new TweetUser();
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser1.setCreatedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser1.setEmail("jane.doe@example.org");
        tweetUser1.setFirstName("Jane");
        tweetUser1.setLastName("Doe");
        tweetUser1.setUserId(123);
        tweetUser1.setUserName("janedoe");
        tweetUser1.setUserPassword("iloveyou");

        MainTweet mainTweet = new MainTweet();
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setCreatedOn(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setEditedOn(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        mainTweet.setLikedUsers(new ArrayList<>());
        mainTweet.setReplyTweets(new ArrayList<>());
        mainTweet.setTweetId(123);
        mainTweet.setTweetString("Tweet String");
        mainTweet.setUser(tweetUser1);
        when(this.tweetCommonService.findByTweetId(anyInt())).thenReturn(mainTweet);
        when(this.tweetCommonService.generateReplyTweetId()).thenReturn(123);

        // Act and Assert
        assertThrows(UserNotFoundException.class,
                () -> this.mainTweetApiServiceImpl.replyToTweet("janedoe", 1, "New Tweet"));
        verify(this.userService).getUserByUserName((String) any());
        verify(this.tweetKafkaProducer).sendMessage((MainTweet) any());
        verify(this.tweetCommonService).findByTweetId(anyInt());
        verify(this.tweetCommonService).generateReplyTweetId();
    }

    /**
     * Method under test: {@link MainTweetApiServiceImpl#replyToTweet(String, int, String)}
     */
    @Test
    void testReplyToTweet3() {
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
        when(this.tweetKafkaProducer.sendMessage((MainTweet) any()))
                .thenThrow(new InvalidInputException("An error occurred"));

        TweetUser tweetUser1 = new TweetUser();
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser1.setCreatedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser1.setEmail("jane.doe@example.org");
        tweetUser1.setFirstName("Jane");
        tweetUser1.setLastName("Doe");
        tweetUser1.setUserId(123);
        tweetUser1.setUserName("janedoe");
        tweetUser1.setUserPassword("iloveyou");

        MainTweet mainTweet = new MainTweet();
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setCreatedOn(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setEditedOn(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        mainTweet.setLikedUsers(new ArrayList<>());
        mainTweet.setReplyTweets(new ArrayList<>());
        mainTweet.setTweetId(123);
        mainTweet.setTweetString("Tweet String");
        mainTweet.setUser(tweetUser1);
        when(this.tweetCommonService.findByTweetId(anyInt())).thenReturn(mainTweet);
        when(this.tweetCommonService.generateReplyTweetId()).thenReturn(123);

        // Act and Assert
        assertThrows(InvalidInputException.class,
                () -> this.mainTweetApiServiceImpl.replyToTweet("janedoe", 1, "New Tweet"));
        verify(this.userService).getUserByUserName((String) any());
        verify(this.tweetKafkaProducer).sendMessage((MainTweet) any());
        verify(this.tweetCommonService).findByTweetId(anyInt());
        verify(this.tweetCommonService).generateReplyTweetId();
    }

    /**
     * Method under test: {@link MainTweetApiServiceImpl#likeTweet(String, int)}
     */
    @Test
    void testLikeTweet() throws ParseException {
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

        TweetUser tweetUser1 = new TweetUser();
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser1.setCreatedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser1.setEmail("jane.doe@example.org");
        tweetUser1.setFirstName("Jane");
        tweetUser1.setLastName("Doe");
        tweetUser1.setUserId(123);
        tweetUser1.setUserName("janedoe");
        tweetUser1.setUserPassword("iloveyou");

        MainTweet mainTweet = new MainTweet();
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setCreatedOn(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setEditedOn(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        mainTweet.setLikedUsers(new ArrayList<>());
        mainTweet.setReplyTweets(new ArrayList<>());
        mainTweet.setTweetId(123);
        mainTweet.setTweetString("Tweet String");
        mainTweet.setUser(tweetUser1);

        TweetUser tweetUser2 = new TweetUser();
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser2.setCreatedDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser2.setEmail("jane.doe@example.org");
        tweetUser2.setFirstName("Jane");
        tweetUser2.setLastName("Doe");
        tweetUser2.setUserId(123);
        tweetUser2.setUserName("janedoe");
        tweetUser2.setUserPassword("iloveyou");

        MainTweet mainTweet1 = new MainTweet();
        LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
        Date fromResult = Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant());
        mainTweet1.setCreatedOn(fromResult);
        LocalDateTime atStartOfDayResult6 = LocalDate.of(1970, 1, 1).atStartOfDay();
        Date fromResult1 = Date.from(atStartOfDayResult6.atZone(ZoneId.of("UTC")).toInstant());
        mainTweet1.setEditedOn(fromResult1);
        mainTweet1.setLikedUsers(new ArrayList<>());
        mainTweet1.setReplyTweets(new ArrayList<>());
        mainTweet1.setTweetId(123);
        mainTweet1.setTweetString("Tweet String");
        mainTweet1.setUser(tweetUser2);
        when(this.tweetCommonService.saveUpdateTweet((MainTweet) any())).thenReturn(mainTweet1);
        when(this.tweetCommonService.findByTweetId(anyInt())).thenReturn(mainTweet);

        // Act
        MainTweetVo actualLikeTweetResult = this.mainTweetApiServiceImpl.likeTweet("janedoe", 1);

        // Assert
        assertSame(fromResult, actualLikeTweetResult.getCreatedOn());
        assertEquals("Tweet String", actualLikeTweetResult.getTweetString());
        assertSame(fromResult1, actualLikeTweetResult.getEditedOn());
        assertTrue(actualLikeTweetResult.getReplyTweets().isEmpty());
        assertEquals(123, actualLikeTweetResult.getTweetId().intValue());
        assertTrue(actualLikeTweetResult.getLikedUsers().isEmpty());
        UserVo user = actualLikeTweetResult.getUser();
        assertEquals("Jane", user.getFirstName());
        assertEquals("jane.doe@example.org", user.getEmail());
        assertEquals("janedoe", user.getUserName());
        assertEquals("iloveyou", user.getUserPassword());
        assertEquals("Doe", user.getLastName());
        assertEquals(123, user.getUserId().intValue());
        verify(this.userService).getUserByUserName((String) any());
        verify(this.tweetCommonService).findByTweetId(anyInt());
        verify(this.tweetCommonService).saveUpdateTweet((MainTweet) any());
    }

    /**
     * Method under test: {@link MainTweetApiServiceImpl#likeTweet(String, int)}
     */
    @Test
    void testLikeTweet2() throws ParseException {
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

        TweetUser tweetUser1 = new TweetUser();
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser1.setCreatedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser1.setEmail("jane.doe@example.org");
        tweetUser1.setFirstName("Jane");
        tweetUser1.setLastName("Doe");
        tweetUser1.setUserId(123);
        tweetUser1.setUserName("janedoe");
        tweetUser1.setUserPassword("iloveyou");

        MainTweet mainTweet = new MainTweet();
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setCreatedOn(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setEditedOn(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        mainTweet.setLikedUsers(new ArrayList<>());
        mainTweet.setReplyTweets(new ArrayList<>());
        mainTweet.setTweetId(123);
        mainTweet.setTweetString("Tweet String");
        mainTweet.setUser(tweetUser1);
        when(this.tweetCommonService.saveUpdateTweet((MainTweet) any()))
                .thenThrow(new UserNotFoundException("An error occurred"));
        when(this.tweetCommonService.findByTweetId(anyInt())).thenReturn(mainTweet);

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> this.mainTweetApiServiceImpl.likeTweet("janedoe", 1));
        verify(this.userService).getUserByUserName((String) any());
        verify(this.tweetCommonService).findByTweetId(anyInt());
        verify(this.tweetCommonService).saveUpdateTweet((MainTweet) any());
    }

    /**
     * Method under test: {@link MainTweetApiServiceImpl#likeTweet(String, int)}
     */
    @Test
    void testLikeTweet3() throws ParseException {
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

        TweetUser tweetUser1 = new TweetUser();
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser1.setCreatedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser1.setEmail("jane.doe@example.org");
        tweetUser1.setFirstName("Jane");
        tweetUser1.setLastName("Doe");
        tweetUser1.setUserId(123);
        tweetUser1.setUserName("janedoe");
        tweetUser1.setUserPassword("iloveyou");

        MainTweet mainTweet = new MainTweet();
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setCreatedOn(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setEditedOn(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        mainTweet.setLikedUsers(new ArrayList<>());
        mainTweet.setReplyTweets(new ArrayList<>());
        mainTweet.setTweetId(123);
        mainTweet.setTweetString("Tweet String");
        mainTweet.setUser(tweetUser1);
        when(this.tweetCommonService.saveUpdateTweet((MainTweet) any()))
                .thenThrow(new InvalidInputException("An error occurred"));
        when(this.tweetCommonService.findByTweetId(anyInt())).thenReturn(mainTweet);

        // Act and Assert
        assertThrows(InvalidInputException.class, () -> this.mainTweetApiServiceImpl.likeTweet("janedoe", 1));
        verify(this.userService).getUserByUserName((String) any());
        verify(this.tweetCommonService).findByTweetId(anyInt());
        verify(this.tweetCommonService).saveUpdateTweet((MainTweet) any());
    }

    /**
     * Method under test: {@link MainTweetApiServiceImpl#likeTweet(String, int)}
     */
    @Test
    void testLikeTweet4() throws ParseException {
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

        TweetUser tweetUser1 = new TweetUser();
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser1.setCreatedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser1.setEmail("jane.doe@example.org");
        tweetUser1.setFirstName("Jane");
        tweetUser1.setLastName("Doe");
        tweetUser1.setUserId(123);
        tweetUser1.setUserName("janedoe");
        tweetUser1.setUserPassword("iloveyou");

        MainTweet mainTweet = new MainTweet();
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setCreatedOn(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setEditedOn(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        mainTweet.setLikedUsers(new ArrayList<>());
        mainTweet.setReplyTweets(new ArrayList<>());
        mainTweet.setTweetId(123);
        mainTweet.setTweetString("Tweet String");
        mainTweet.setUser(tweetUser1);

        TweetUser tweetUser2 = new TweetUser();
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser2.setCreatedDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser2.setEmail("jane.doe@example.org");
        tweetUser2.setFirstName("Jane");
        tweetUser2.setLastName("Doe");
        tweetUser2.setUserId(123);
        tweetUser2.setUserName("janedoe");
        tweetUser2.setUserPassword("iloveyou");

        TweetUser tweetUser3 = new TweetUser();
        LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser3.setCreatedDate(Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser3.setEmail("jane.doe@example.org");
        tweetUser3.setFirstName("Jane");
        tweetUser3.setLastName("Doe");
        tweetUser3.setUserId(123);
        tweetUser3.setUserName("janedoe");
        tweetUser3.setUserPassword("iloveyou");
        MainTweet mainTweet1 = mock(MainTweet.class);
        when(mainTweet1.getReplyTweets()).thenThrow(new UserNotFoundException("An error occurred"));
        when(mainTweet1.getUser()).thenReturn(tweetUser3);
        when(mainTweet1.getTweetId()).thenReturn(123);
        when(mainTweet1.getTweetString()).thenReturn("Tweet String");
        LocalDateTime atStartOfDayResult6 = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(mainTweet1.getCreatedOn()).thenReturn(Date.from(atStartOfDayResult6.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult7 = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(mainTweet1.getEditedOn()).thenReturn(Date.from(atStartOfDayResult7.atZone(ZoneId.of("UTC")).toInstant()));
        when(mainTweet1.getLikedUsers()).thenReturn(new ArrayList<>());
        doNothing().when(mainTweet1).setCreatedOn((Date) any());
        doNothing().when(mainTweet1).setEditedOn((Date) any());
        doNothing().when(mainTweet1).setLikedUsers((List<TweetUser>) any());
        doNothing().when(mainTweet1).setReplyTweets((List<ReplyTweet>) any());
        doNothing().when(mainTweet1).setTweetId((Integer) any());
        doNothing().when(mainTweet1).setTweetString((String) any());
        doNothing().when(mainTweet1).setUser((TweetUser) any());
        LocalDateTime atStartOfDayResult8 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet1.setCreatedOn(Date.from(atStartOfDayResult8.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult9 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet1.setEditedOn(Date.from(atStartOfDayResult9.atZone(ZoneId.of("UTC")).toInstant()));
        mainTweet1.setLikedUsers(new ArrayList<>());
        mainTweet1.setReplyTweets(new ArrayList<>());
        mainTweet1.setTweetId(123);
        mainTweet1.setTweetString("Tweet String");
        mainTweet1.setUser(tweetUser2);
        when(this.tweetCommonService.saveUpdateTweet((MainTweet) any())).thenReturn(mainTweet1);
        when(this.tweetCommonService.findByTweetId(anyInt())).thenReturn(mainTweet);

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> this.mainTweetApiServiceImpl.likeTweet("janedoe", 1));
        verify(this.userService).getUserByUserName((String) any());
        verify(this.tweetCommonService).findByTweetId(anyInt());
        verify(this.tweetCommonService).saveUpdateTweet((MainTweet) any());
        verify(mainTweet1).getUser();
        verify(mainTweet1).getTweetId();
        verify(mainTweet1).getTweetString();
        verify(mainTweet1).getCreatedOn();
        verify(mainTweet1).getEditedOn();
        verify(mainTweet1).getLikedUsers();
        verify(mainTweet1).getReplyTweets();
        verify(mainTweet1).setCreatedOn((Date) any());
        verify(mainTweet1).setEditedOn((Date) any());
        verify(mainTweet1).setLikedUsers((List<TweetUser>) any());
        verify(mainTweet1).setReplyTweets((List<ReplyTweet>) any());
        verify(mainTweet1).setTweetId((Integer) any());
        verify(mainTweet1).setTweetString((String) any());
        verify(mainTweet1).setUser((TweetUser) any());
    }

    /**
     * Method under test: {@link MainTweetApiServiceImpl#likeTweet(String, int)}
     */
    @Test
    void testLikeTweet5() throws ParseException {
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

        TweetUser tweetUser1 = new TweetUser();
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser1.setCreatedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser1.setEmail("jane.doe@example.org");
        tweetUser1.setFirstName("Jane");
        tweetUser1.setLastName("Doe");
        tweetUser1.setUserId(123);
        tweetUser1.setUserName("janedoe");
        tweetUser1.setUserPassword("iloveyou");

        MainTweet mainTweet = new MainTweet();
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setCreatedOn(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setEditedOn(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        mainTweet.setLikedUsers(new ArrayList<>());
        mainTweet.setReplyTweets(new ArrayList<>());
        mainTweet.setTweetId(123);
        mainTweet.setTweetString("Tweet String");
        mainTweet.setUser(tweetUser1);

        TweetUser tweetUser2 = new TweetUser();
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser2.setCreatedDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser2.setEmail("jane.doe@example.org");
        tweetUser2.setFirstName("Jane");
        tweetUser2.setLastName("Doe");
        tweetUser2.setUserId(123);
        tweetUser2.setUserName("janedoe");
        tweetUser2.setUserPassword("iloveyou");

        TweetUser tweetUser3 = new TweetUser();
        LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser3.setCreatedDate(Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser3.setEmail("jane.doe@example.org");
        tweetUser3.setFirstName("Jane");
        tweetUser3.setLastName("Doe");
        tweetUser3.setUserId(123);
        tweetUser3.setUserName("janedoe");
        tweetUser3.setUserPassword("iloveyou");
        MainTweet mainTweet1 = mock(MainTweet.class);
        when(mainTweet1.getReplyTweets()).thenThrow(new InvalidInputException("An error occurred"));
        when(mainTweet1.getUser()).thenReturn(tweetUser3);
        when(mainTweet1.getTweetId()).thenReturn(123);
        when(mainTweet1.getTweetString()).thenReturn("Tweet String");
        LocalDateTime atStartOfDayResult6 = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(mainTweet1.getCreatedOn()).thenReturn(Date.from(atStartOfDayResult6.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult7 = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(mainTweet1.getEditedOn()).thenReturn(Date.from(atStartOfDayResult7.atZone(ZoneId.of("UTC")).toInstant()));
        when(mainTweet1.getLikedUsers()).thenReturn(new ArrayList<>());
        doNothing().when(mainTweet1).setCreatedOn((Date) any());
        doNothing().when(mainTweet1).setEditedOn((Date) any());
        doNothing().when(mainTweet1).setLikedUsers((List<TweetUser>) any());
        doNothing().when(mainTweet1).setReplyTweets((List<ReplyTweet>) any());
        doNothing().when(mainTweet1).setTweetId((Integer) any());
        doNothing().when(mainTweet1).setTweetString((String) any());
        doNothing().when(mainTweet1).setUser((TweetUser) any());
        LocalDateTime atStartOfDayResult8 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet1.setCreatedOn(Date.from(atStartOfDayResult8.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult9 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet1.setEditedOn(Date.from(atStartOfDayResult9.atZone(ZoneId.of("UTC")).toInstant()));
        mainTweet1.setLikedUsers(new ArrayList<>());
        mainTweet1.setReplyTweets(new ArrayList<>());
        mainTweet1.setTweetId(123);
        mainTweet1.setTweetString("Tweet String");
        mainTweet1.setUser(tweetUser2);
        when(this.tweetCommonService.saveUpdateTweet((MainTweet) any())).thenReturn(mainTweet1);
        when(this.tweetCommonService.findByTweetId(anyInt())).thenReturn(mainTweet);

        // Act and Assert
        assertThrows(InvalidInputException.class, () -> this.mainTweetApiServiceImpl.likeTweet("janedoe", 1));
        verify(this.userService).getUserByUserName((String) any());
        verify(this.tweetCommonService).findByTweetId(anyInt());
        verify(this.tweetCommonService).saveUpdateTweet((MainTweet) any());
        verify(mainTweet1).getUser();
        verify(mainTweet1).getTweetId();
        verify(mainTweet1).getTweetString();
        verify(mainTweet1).getCreatedOn();
        verify(mainTweet1).getEditedOn();
        verify(mainTweet1).getLikedUsers();
        verify(mainTweet1).getReplyTweets();
        verify(mainTweet1).setCreatedOn((Date) any());
        verify(mainTweet1).setEditedOn((Date) any());
        verify(mainTweet1).setLikedUsers((List<TweetUser>) any());
        verify(mainTweet1).setReplyTweets((List<ReplyTweet>) any());
        verify(mainTweet1).setTweetId((Integer) any());
        verify(mainTweet1).setTweetString((String) any());
        verify(mainTweet1).setUser((TweetUser) any());
    }

    /**
     * Method under test: {@link MainTweetApiServiceImpl#getTweetsByUserName(String)}
     */
    @Test
    void testGetTweetsByUserName() {
        // Arrange
        when(this.tweetCommonService.getTweetsByUserName((String) any())).thenReturn(new ArrayList<>());

        // Act and Assert
        assertTrue(this.mainTweetApiServiceImpl.getTweetsByUserName("janedoe").isEmpty());
        verify(this.tweetCommonService).getTweetsByUserName((String) any());
    }

    /**
     * Method under test: {@link MainTweetApiServiceImpl#getTweetsByUserName(String)}
     */
    @Test
    void testGetTweetsByUserName2() {
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

        ArrayList<MainTweet> mainTweetList = new ArrayList<>();
        mainTweetList.add(mainTweet);
        when(this.tweetCommonService.getTweetsByUserName((String) any())).thenReturn(mainTweetList);

        // Act and Assert
        assertEquals(1, this.mainTweetApiServiceImpl.getTweetsByUserName("janedoe").size());
        verify(this.tweetCommonService).getTweetsByUserName((String) any());
    }

    /**
     * Method under test: {@link MainTweetApiServiceImpl#getTweetsByUserName(String)}
     */
    @Test
    void testGetTweetsByUserName3() {
        // Arrange
        when(this.tweetCommonService.getTweetsByUserName((String) any()))
                .thenThrow(new UserNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> this.mainTweetApiServiceImpl.getTweetsByUserName("janedoe"));
        verify(this.tweetCommonService).getTweetsByUserName((String) any());
    }

    /**
     * Method under test: {@link MainTweetApiServiceImpl#getTweetsByUserName(String)}
     */
    @Test
    void testGetTweetsByUserName4() {
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

        TweetUser tweetUser1 = new TweetUser();
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser1.setCreatedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser1.setEmail("jane.doe@example.org");
        tweetUser1.setFirstName("Jane");
        tweetUser1.setLastName("Doe");
        tweetUser1.setUserId(123);
        tweetUser1.setUserName("janedoe");
        tweetUser1.setUserPassword("iloveyou");
        MainTweet mainTweet = mock(MainTweet.class);
        when(mainTweet.getReplyTweets()).thenThrow(new UserNotFoundException("An error occurred"));
        when(mainTweet.getUser()).thenReturn(tweetUser1);
        when(mainTweet.getTweetId()).thenReturn(123);
        when(mainTweet.getTweetString()).thenReturn("Tweet String");
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(mainTweet.getCreatedOn()).thenReturn(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(mainTweet.getEditedOn()).thenReturn(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        when(mainTweet.getLikedUsers()).thenReturn(new ArrayList<>());
        doNothing().when(mainTweet).setCreatedOn((Date) any());
        doNothing().when(mainTweet).setEditedOn((Date) any());
        doNothing().when(mainTweet).setLikedUsers((List<TweetUser>) any());
        doNothing().when(mainTweet).setReplyTweets((List<ReplyTweet>) any());
        doNothing().when(mainTweet).setTweetId((Integer) any());
        doNothing().when(mainTweet).setTweetString((String) any());
        doNothing().when(mainTweet).setUser((TweetUser) any());
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setCreatedOn(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setEditedOn(Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant()));
        mainTweet.setLikedUsers(new ArrayList<>());
        mainTweet.setReplyTweets(new ArrayList<>());
        mainTweet.setTweetId(123);
        mainTweet.setTweetString("Tweet String");
        mainTweet.setUser(tweetUser);

        ArrayList<MainTweet> mainTweetList = new ArrayList<>();
        mainTweetList.add(mainTweet);
        when(this.tweetCommonService.getTweetsByUserName((String) any())).thenReturn(mainTweetList);

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> this.mainTweetApiServiceImpl.getTweetsByUserName("janedoe"));
        verify(this.tweetCommonService).getTweetsByUserName((String) any());
        verify(mainTweet).getUser();
        verify(mainTweet).getTweetId();
        verify(mainTweet).getTweetString();
        verify(mainTweet).getCreatedOn();
        verify(mainTweet).getEditedOn();
        verify(mainTweet).getLikedUsers();
        verify(mainTweet).getReplyTweets();
        verify(mainTweet).setCreatedOn((Date) any());
        verify(mainTweet).setEditedOn((Date) any());
        verify(mainTweet).setLikedUsers((List<TweetUser>) any());
        verify(mainTweet).setReplyTweets((List<ReplyTweet>) any());
        verify(mainTweet).setTweetId((Integer) any());
        verify(mainTweet).setTweetString((String) any());
        verify(mainTweet).setUser((TweetUser) any());
    }

    /**
     * Method under test: {@link MainTweetApiServiceImpl#viewAllTweets()}
     */
    @Test
    void testViewAllTweets() {
        // Arrange
        when(this.tweetCommonService.viewAllTweets()).thenReturn(new ArrayList<>());

        // Act and Assert
        assertTrue(this.mainTweetApiServiceImpl.viewAllTweets().isEmpty());
        verify(this.tweetCommonService).viewAllTweets();
    }

    /**
     * Method under test: {@link MainTweetApiServiceImpl#viewAllTweets()}
     */
    @Test
    void testViewAllTweets2() {
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

        ArrayList<MainTweet> mainTweetList = new ArrayList<>();
        mainTweetList.add(mainTweet);
        when(this.tweetCommonService.viewAllTweets()).thenReturn(mainTweetList);

        // Act and Assert
        assertEquals(1, this.mainTweetApiServiceImpl.viewAllTweets().size());
        verify(this.tweetCommonService).viewAllTweets();
    }

    /**
     * Method under test: {@link MainTweetApiServiceImpl#viewAllTweets()}
     */
    @Test
    void testViewAllTweets3() {
        // Arrange
        when(this.tweetCommonService.viewAllTweets()).thenThrow(new UserNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> this.mainTweetApiServiceImpl.viewAllTweets());
        verify(this.tweetCommonService).viewAllTweets();
    }

    /**
     * Method under test: {@link MainTweetApiServiceImpl#viewAllTweets()}
     */
    @Test
    void testViewAllTweets4() {
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

        TweetUser tweetUser1 = new TweetUser();
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        tweetUser1.setCreatedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
        tweetUser1.setEmail("jane.doe@example.org");
        tweetUser1.setFirstName("Jane");
        tweetUser1.setLastName("Doe");
        tweetUser1.setUserId(123);
        tweetUser1.setUserName("janedoe");
        tweetUser1.setUserPassword("iloveyou");
        MainTweet mainTweet = mock(MainTweet.class);
        when(mainTweet.getReplyTweets()).thenThrow(new UserNotFoundException("An error occurred"));
        when(mainTweet.getUser()).thenReturn(tweetUser1);
        when(mainTweet.getTweetId()).thenReturn(123);
        when(mainTweet.getTweetString()).thenReturn("Tweet String");
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(mainTweet.getCreatedOn()).thenReturn(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(mainTweet.getEditedOn()).thenReturn(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        when(mainTweet.getLikedUsers()).thenReturn(new ArrayList<>());
        doNothing().when(mainTweet).setCreatedOn((Date) any());
        doNothing().when(mainTweet).setEditedOn((Date) any());
        doNothing().when(mainTweet).setLikedUsers((List<TweetUser>) any());
        doNothing().when(mainTweet).setReplyTweets((List<ReplyTweet>) any());
        doNothing().when(mainTweet).setTweetId((Integer) any());
        doNothing().when(mainTweet).setTweetString((String) any());
        doNothing().when(mainTweet).setUser((TweetUser) any());
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setCreatedOn(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mainTweet.setEditedOn(Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant()));
        mainTweet.setLikedUsers(new ArrayList<>());
        mainTweet.setReplyTweets(new ArrayList<>());
        mainTweet.setTweetId(123);
        mainTweet.setTweetString("Tweet String");
        mainTweet.setUser(tweetUser);

        ArrayList<MainTweet> mainTweetList = new ArrayList<>();
        mainTweetList.add(mainTweet);
        when(this.tweetCommonService.viewAllTweets()).thenReturn(mainTweetList);

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> this.mainTweetApiServiceImpl.viewAllTweets());
        verify(this.tweetCommonService).viewAllTweets();
        verify(mainTweet).getUser();
        verify(mainTweet).getTweetId();
        verify(mainTweet).getTweetString();
        verify(mainTweet).getCreatedOn();
        verify(mainTweet).getEditedOn();
        verify(mainTweet).getLikedUsers();
        verify(mainTweet).getReplyTweets();
        verify(mainTweet).setCreatedOn((Date) any());
        verify(mainTweet).setEditedOn((Date) any());
        verify(mainTweet).setLikedUsers((List<TweetUser>) any());
        verify(mainTweet).setReplyTweets((List<ReplyTweet>) any());
        verify(mainTweet).setTweetId((Integer) any());
        verify(mainTweet).setTweetString((String) any());
        verify(mainTweet).setUser((TweetUser) any());
    }
}

