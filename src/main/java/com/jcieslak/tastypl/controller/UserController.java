package com.jcieslak.tastypl.controller;

import com.jcieslak.tastypl.payload.response.UserResponse;
import com.jcieslak.tastypl.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("{userId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CUSTOMER')")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable("userId") Long userId){
        UserResponse userResponse = userService.getUserProfile(userId);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
}
