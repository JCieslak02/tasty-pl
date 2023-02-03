package com.jcieslak.tastypl.service;

import com.jcieslak.tastypl.enums.UserRole;
import com.jcieslak.tastypl.exception.NotFoundException;
import com.jcieslak.tastypl.exception.PrincipalIsNotAnOwnerException;
import com.jcieslak.tastypl.mapper.UserMapper;
import com.jcieslak.tastypl.model.User;
import com.jcieslak.tastypl.payload.response.UserResponse;
import com.jcieslak.tastypl.repository.UserRepository;
import com.jcieslak.tastypl.security.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;
    private final UserMapper userMapper;
    private final OrderService orderService;

    public UserResponse getUserProfile(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user", userId));

        User currentUser = authService.getPrincipal();

        if(user.getRole().equals(UserRole.ROLE_CUSTOMER) && !Objects.equals(user, currentUser)){
            throw new PrincipalIsNotAnOwnerException();
        }

        UserResponse userResponse = userMapper.toUserResponse(user);
        userResponse.setOrderResponseList(orderService.getAllOrdersByUserId(userId));

        return userResponse;
    }
}
