package com.jcieslak.tastypl.service;

import com.jcieslak.tastypl.exception.FieldExistsInDatabase;
import com.jcieslak.tastypl.model.User;
import com.jcieslak.tastypl.payload.request.LoginRequest;
import com.jcieslak.tastypl.payload.request.SignupRequest;
import com.jcieslak.tastypl.payload.response.JwtResponse;
import com.jcieslak.tastypl.payload.response.SignUpResponse;
import com.jcieslak.tastypl.repository.UserRepository;
import com.jcieslak.tastypl.security.jwt.JwtUtils;
import com.jcieslak.tastypl.util.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.jcieslak.tastypl.enums.UserRole.ROLE_ADMIN;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    private final UserMapper userMapper;
    public JwtResponse signIn(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User user = (User) authentication.getPrincipal();

        return new JwtResponse(jwt, "Bearer", user.getId(), user.getUsername(), user.getRole());
    }
    public SignUpResponse createUser(SignupRequest signupRequest){
        if(userRepository.existsByEmail(signupRequest.getEmail())){
            throw new FieldExistsInDatabase("user", "email");
        }

        if(userRepository.existsByUsername(signupRequest.getUsername())){
            throw new FieldExistsInDatabase("user", "username");
        }

        if(userRepository.existsByPhoneNumber(signupRequest.getPhoneNumber())){
            throw new FieldExistsInDatabase("user", "phone number");
        }

        if(signupRequest.getUserRole() == ROLE_ADMIN){
            throw new IllegalArgumentException("Admin role can only be granted to existing users");
        }

        User user = userMapper.toEntity(signupRequest);
        user.setPassword(encoder.encode(signupRequest.getPassword()));

        userRepository.save(user);

        return userMapper.toResponse(user);
    }
}
