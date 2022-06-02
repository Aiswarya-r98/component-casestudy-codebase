package com.tweetapp.api.middleware;

import com.tweetapp.common.model.MainTweet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

@Component
@Slf4j
public class TweetKafkaProducer {

    @Autowired
    private KafkaTemplate<String, MainTweet> kafkaTemplate;

    @Value("${messageapp.kafka.topic}")
    private String kafkaTopic;

    public ListenableFuture<SendResult<String, MainTweet>> sendMessage(MainTweet message) {

        return this.kafkaTemplate.send(kafkaTopic, message);
    }
}