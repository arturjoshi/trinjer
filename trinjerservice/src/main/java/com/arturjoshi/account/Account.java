package com.arturjoshi.account;

import com.arturjoshi.project.Project;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by arturjoshi on 04-Jan-17.
 */
@NoArgsConstructor
@Data
@Entity
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Embedded
    private AccountCredentials credentials = new AccountCredentials();
    @ManyToMany(mappedBy = "members")
    private Set<Project> projects;
    @ManyToMany(mappedBy = "invitations")
    private Set<Project> projectInvitations;
    private String createdTime = LocalDateTime.now().toString();

    public Account(Account account) {
        this.id = account.id;
        this.username = account.username;
        this.email = account.email;
        this.credentials.setPassword(account.getCredentials().getPassword());
    }
}
