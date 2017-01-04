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
    @Id @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String username;
    @Embedded
    private AccountCredentials accountCredentials;
}
