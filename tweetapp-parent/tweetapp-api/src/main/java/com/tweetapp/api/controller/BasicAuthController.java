package com.tweetapp.api.controller;

import com.tweetapp.api.security.config.JwtUtil;
import com.tweetapp.api.security.service.UserApiSecurityService;
import com.tweetapp.common.model.AuthenticationRequest;
import com.tweetapp.common.model.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:4200", "http://tweetapp-ui.southindia.azurecontainer.io"})
@RestController
@RequestMapping("/api/v1/tweets")
public class BasicAuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserApiSecurityService userApiSecurityService;


    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(path = "/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticateRequest) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticateRequest.getUserName(), authenticateRequest.getPassword())
            );
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException(ex.getMessage());
        }

        UserDetails userDetails = userApiSecurityService.loadUserByUsername(authenticateRequest.getUserName());
        String token = jwtUtil.generateToken(userDetails);

        return new ResponseEntity<>(new AuthenticationResponse(token), HttpStatus.OK);
    }
}