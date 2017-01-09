package com.arturjoshi.authentication.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by arturjoshi on 04-Jan-17.
 */
@NoArgsConstructor
@Data
public class AccountRegistrationDto {

    private String username;
    private String email;
    private String password;
}
