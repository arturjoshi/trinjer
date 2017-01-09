package com.arturjoshi.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by arturjoshi on 04-Jan-17.
 */
@NoArgsConstructor
@Data
@Embeddable
public class AccountCredentials {

    @JsonIgnore
    private String password;
}
