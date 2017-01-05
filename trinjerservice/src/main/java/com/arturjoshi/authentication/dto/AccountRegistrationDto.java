package com.arturjoshi.authentication.dto;

import com.arturjoshi.account.Account;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

/**
 * Created by arturjoshi on 04-Jan-17.
 */
@NoArgsConstructor
@Data
public class AccountRegistrationDto {
    private String username;
    private String email;
    private String password;

    public Account getAccountFromDto() {
        Account account = new Account();
        account.setUsername(username);
        account.setEmail(email);
        account.getCredentials().setPassword(new ShaPasswordEncoder(256).encodePassword(password, null));
        return account;
    }
}
