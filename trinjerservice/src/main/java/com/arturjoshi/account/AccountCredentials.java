package com.arturjoshi.account;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

/**
 * Created by arturjoshi on 04-Jan-17.
 */
@NoArgsConstructor
@Data
@Embeddable
public class AccountCredentials {
    private String email;
    private String password;
}
