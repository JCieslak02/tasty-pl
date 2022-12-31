package com.jcieslak.tastypl.service;

import com.jcieslak.tastypl.exception.FieldExistsInDatabase;
import com.jcieslak.tastypl.model.User;
import com.jcieslak.tastypl.payload.request.LoginRequest;
import com.jcieslak.tastypl.payload.request.SignupRequest;
import com.jcieslak.tastypl.payload.response.JwtResponse;
import com.jcieslak.tastypl.payload.response.SignUpResponse;
import com.jcieslak.tastypl.repository.UserRepository;
import com.jcieslak.tastypl.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.jcieslak.tastypl.enums.UserRole.ROLE_CUSTOMER;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    private final ModelMapper modelMapper;
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

        modelMapper.createTypeMap(SignupRequest.class, User.class)
                .addMappings(m -> m.skip(User::setPassword))
                .addMappings(m -> m.skip(User::setRole));

        User user = modelMapper.map(signupRequest, User.class);
        user.setPassword(encoder.encode(signupRequest.getPassword()));
        user.setRole(ROLE_CUSTOMER);

        userRepository.save(user);

        return modelMapper.map(user, SignUpResponse.class);
    }
}
