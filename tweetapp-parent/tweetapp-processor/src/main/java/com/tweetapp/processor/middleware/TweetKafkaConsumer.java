package com.tweetapp.processor.middleware;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tweetapp.common.model.MainTweet;
import com.tweetapp.processor.service.MainTweetProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TweetKafkaConsumer {

    @Autowired
    private MainTweetProcessingService tweetProcessingService;

    @KafkaListener(topics = "${messageapp.kafka.topic}", groupId = "${messageapp.kafka.groupid}")
    public void consume(final @Payload MainTweet message,
                        final Acknowledgment acknowledgment) throws JsonProcessingException {
        log.info("#### -> Consumed message -> {}", message);
        acknowledgment.acknowledge();
        tweetProcessingService.processTweet(message);
    }
}