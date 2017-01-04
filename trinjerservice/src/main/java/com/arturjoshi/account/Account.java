package com.arturjoshi.account;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by arturjoshi on 04-Jan-17.
 */
@NoArgsConstructor
@Data
@Entity
public class Account {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Embedded
    private AccountCredentials credentials = new AccountCredentials();

    public Account(Account account) {
        this.id = account.id;
        this.username = account.username;
        this.email = account.email;
        this.credentials.setPassword(account.getCredentials().getPassword());
    }
}
