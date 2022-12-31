package com.jcieslak.tastypl.payload.response;

import com.jcieslak.tastypl.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class JwtResponse {
    private String token;
    private String type;
    private Long id;
    private String username;
    private UserRole userRole;
}
