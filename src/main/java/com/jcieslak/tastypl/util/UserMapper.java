package com.jcieslak.tastypl.util;

import com.jcieslak.tastypl.model.User;
import com.jcieslak.tastypl.payload.request.SignupRequest;
import com.jcieslak.tastypl.payload.response.SignUpResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final ModelMapper modelMapper;

    public UserMapper() {
        this.modelMapper = new ModelMapper();
        modelMapper.createTypeMap(SignupRequest.class, User.class)
                .addMappings(m -> m.skip(User::setPassword));
    }

    public User toEntity(SignupRequest signupRequest){
        return modelMapper.map(signupRequest, User.class);
    }

    public SignUpResponse toResponse(User user){
        return modelMapper.map(user, SignUpResponse.class);
    }
}
