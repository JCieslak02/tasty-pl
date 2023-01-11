package com.jcieslak.tastypl.payload.request;

import com.jcieslak.tastypl.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Setter
public class SignupRequest {
    @NotNull
    private String username;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String password;
    @NotNull
    private UserRole userRole;
}
