package com.jcieslak.tastypl.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusUpdateRequest {
    @NotNull
    private String orderStatus;
}
