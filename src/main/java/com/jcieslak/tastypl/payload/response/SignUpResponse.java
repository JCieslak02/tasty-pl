package com.jcieslak.tastypl.payload.response;

import com.jcieslak.tastypl.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class SignUpResponse {
    private String firstName;
    private String lastName;
    private String username;
    private UserRole userRole;
    private String email;
    private String phoneNumber;
}
