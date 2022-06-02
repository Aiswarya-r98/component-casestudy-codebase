package com.tweetapp.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan({"com.tweetapp.*"})
@EntityScan("com.tweetapp.common.model")
@EnableMongoRepositories("com.tweetapp.common.repository")
public class TweetappProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(TweetappProcessorApplication.class, args);
	}

}
