package com.jcieslak.tastypl.security.config;

import com.jcieslak.tastypl.model.User;
import com.jcieslak.tastypl.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username){
        return userRepository.findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }


}
