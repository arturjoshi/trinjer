package com.arturjoshi.authentication.dto;

import com.arturjoshi.account.Account;
import lombok.Data;

/**
 * Created by arturjoshi on 04-Jan-17.
 */
@Data
public class AccountAuthenticationDto {

    private Long id;
    private String username;
    private String email;
    private String token;

    public AccountAuthenticationDto(Account account, String token) {
        this.id = account.getId();
        this.username = account.getUsername();
        this.email = account.getEmail();
        this.token = token;
    }

}
