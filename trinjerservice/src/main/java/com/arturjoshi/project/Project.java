package com.arturjoshi.project;

import com.arturjoshi.account.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by arturjoshi on 07-Jan-17.
 */
@NoArgsConstructor
@Data
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    @ManyToOne
    private Account projectOwner;
    @ManyToMany
    private Set<Account> members;
    @ManyToMany
    private Set<Account> invitations;
    @ManyToMany
    @MapKeyJoinColumn(name = "account_id")
    @JoinTable(name = "project_account",
            joinColumns = {@JoinColumn(name = "project_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Map<Account, ProjectRole> accountsRoles = new HashMap<>();
}
