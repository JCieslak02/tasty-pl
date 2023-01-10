package com.jcieslak.tastypl.security.auth;

import com.jcieslak.tastypl.payload.response.JwtResponse;
import com.jcieslak.tastypl.payload.request.LoginRequest;
import com.jcieslak.tastypl.payload.response.SignUpResponse;
import com.jcieslak.tastypl.payload.request.SignupRequest;
import com.jcieslak.tastypl.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = userService.signIn(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> registerUser(@RequestBody SignupRequest signUpRequest) {
        SignUpResponse signUpResponse = userService.createUser(signUpRequest);
        return new ResponseEntity<>(signUpResponse, HttpStatus.CREATED);
    }
}
