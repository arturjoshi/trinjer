package com.arturjoshi.authentication.dto;

import com.arturjoshi.account.Account;
import lombok.Data;

/**
 * Created by arturjoshi on 04-Jan-17.
 */
@Data
public class AccountAuthenticationDto {

    private Account account;
    private String token;

    public AccountAuthenticationDto(Account account, String token) {
        this.account = account;
        this.token = token;
    }
}
