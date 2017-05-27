package com.arturjoshi.project.entities;

import com.arturjoshi.account.Account;
import com.arturjoshi.project.Project;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

/**
 * Created by arturjoshi on 09-Jan-17.
 */
@NoArgsConstructor
@RequiredArgsConstructor
@Data
@Entity
public class ProjectAccountPermission {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private @NonNull Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    private @NonNull Account account;

    @Enumerated(EnumType.STRING)
    private @NonNull ProjectPermission projectPermission;

    public enum ProjectPermission {
        REPORTER,
        MEMBER,
        MASTER,
        OWNER
    }
}
