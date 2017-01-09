package com.arturjoshi.account;

import com.arturjoshi.project.Project;
import com.arturjoshi.project.entities.ProjectAccountPermission;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(of = {"username", "email", "credentials", "createdTime", "isConfirmed", "isTemp"})
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Embedded
    private AccountCredentials credentials = new AccountCredentials();

    private String createdTime = LocalDateTime.now().toString();

    private Boolean isConfirmed = false;

    private Boolean isTemp = false;

    @OneToMany(mappedBy = "projectOwner")
    @JsonIgnore
    private Set<Project> projectOwned = new HashSet<>();

    @ManyToMany(mappedBy = "members")
    @JsonIgnore
    private Set<Project> projects = new HashSet<>();

    @ManyToMany(mappedBy = "invitations")
    @JsonIgnore
    private Set<Project> projectInvitations = new HashSet<>();

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    private Set<ProjectAccountPermission> projectAccountPermissions = new HashSet<>();

    public Account(Account account) {
        this.id = account.id;
        this.username = account.username;
        this.email = account.email;
        this.credentials.setPassword(account.getCredentials().getPassword());
        this.isConfirmed = account.isConfirmed;
        this.isTemp = account.isTemp;
    }
}
