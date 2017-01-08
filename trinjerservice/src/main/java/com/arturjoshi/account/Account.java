package com.arturjoshi.account;

import com.arturjoshi.project.Project;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
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
    @OneToMany(mappedBy = "projectOwner")
    private Set<Project> projectOwned = new HashSet<>();
    @ManyToMany(mappedBy = "members")
    private Set<Project> projects = new HashSet<>();
    @ManyToMany(mappedBy = "invitations")
    private Set<Project> projectInvitations = new HashSet<>();
    private String createdTime = LocalDateTime.now().toString();
    private Boolean isActive = false;

    public Account(Account account) {
        this.id = account.id;
        this.username = account.username;
        this.email = account.email;
        this.credentials.setPassword(account.getCredentials().getPassword());
        this.isActive = account.isActive;
    }
}
