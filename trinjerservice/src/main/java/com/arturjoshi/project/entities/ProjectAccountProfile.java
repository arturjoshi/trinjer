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
public class ProjectAccountProfile {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    private Project project;
    @ManyToOne
    private Account account;
    @Enumerated
    private ProjectProfile projectProfile;

    public enum ProjectProfile {
        QA,
        DEVELOPER,
        MANAGER
    }
}
