package com.arturjoshi.project;

import com.arturjoshi.account.Account;
import com.arturjoshi.project.entity.accountpermission.ProjectAccountPermission;
import com.arturjoshi.project.entity.accountprofile.ProjectAccountProfile;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by arturjoshi on 07-Jan-17.
 */
@NoArgsConstructor
@Data
@Entity
@EqualsAndHashCode(exclude = {"members", "invitations", "projectAccountProfiles", "projectAccountPermissions"})
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    @ManyToOne
    private Account projectOwner;
    @ManyToMany
    private Set<Account> members = new HashSet<>();
    @ManyToMany
    private Set<Account> invitations = new HashSet<>();
    @OneToMany(mappedBy = "project")
    private Set<ProjectAccountProfile> projectAccountProfiles = new HashSet<>();
    @OneToMany(mappedBy = "project")
    private Set<ProjectAccountPermission> projectAccountPermissions = new HashSet<>();
}
