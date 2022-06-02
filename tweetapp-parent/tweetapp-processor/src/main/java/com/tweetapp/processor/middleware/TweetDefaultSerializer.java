package com.tweetapp.processor.middleware;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tweetapp.common.model.MainTweet;
import com.tweetapp.common.model.Tweet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
public class TweetDefaultSerializer extends JsonDeserializer<Object> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Object deserialize(String topic, byte[] data) {
        try {
            return objectMapper.readValue(new String(data), MainTweet.class);
        } catch (JsonProcessingException e) {
            log.error("Cannot deserialize data - {} \n", Arrays.toString(data), e);
        }

        return super.deserialize(topic, data);
    }
}
