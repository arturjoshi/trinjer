package com.arturjoshi.project.entities;

import com.arturjoshi.account.Account;
import com.arturjoshi.project.Project;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by arturjoshi on 09-Jan-17.
 */
@NoArgsConstructor
@Data
@Entity
public class ProjectAccountPermission {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @Enumerated(EnumType.STRING)
    private ProjectPermission projectPermission;

    public enum ProjectPermission {
        OWNER,
        MASTER,
        MEMBER,
        REPORTER
    }
}
