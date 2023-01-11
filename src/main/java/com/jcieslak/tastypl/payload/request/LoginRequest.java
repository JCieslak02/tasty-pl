package com.jcieslak.tastypl.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Setter
public class LoginRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
