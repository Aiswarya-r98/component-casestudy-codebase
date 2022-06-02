package com.tweetapp.api.controller;


import com.tweetapp.common.exception.InvalidInputException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin(origins = {"http://localhost:4200","http://tweetapp-ui.southindia.azurecontainer.io"})
@RestController
@RequestMapping("/api/v1/tweets")
public class TestController {

    @GetMapping(path = "/elk")
    @Operation(summary = "Test ELK")
    public ResponseEntity<Void> elk() {
        try {
            throw new InvalidInputException("Please try again later...");
        } catch (InvalidInputException ex) {
            log.error("Test error", ex);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/pong")
    @Operation(summary = "Test Backend App")
    public ResponseEntity<String> pong() {
        return new ResponseEntity<>("PONG.", HttpStatus.OK);
    }
}
