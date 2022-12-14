package org.uvt.MovieRental.service;

import org.uvt.MovieRental.dto.AuthenticationResponse;
import org.uvt.MovieRental.dto.UsernameAndPasswordAuthenticationRequest;
import org.uvt.MovieRental.entity.User;
import org.uvt.MovieRental.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    public ResponseEntity<AuthenticationResponse> authenticateLogin(UsernameAndPasswordAuthenticationRequest authenticationRequest) throws Exception{
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),authenticationRequest.getPassword()));
        }
        catch (Exception e){
            throw new Exception(e);
        }
        final User userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    public ResponseEntity<AuthenticationResponse> authenticateRegister(User user) throws Exception{
        UsernameAndPasswordAuthenticationRequest authenticationRequest = new UsernameAndPasswordAuthenticationRequest(user.getEmail(), user.getPassword());
        if(userService.addUser(user).getStatusCode().value() == 201){
            return authenticateLogin(authenticationRequest);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

}
