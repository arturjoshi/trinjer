package com.arturjoshi.issues;

import com.arturjoshi.account.Account;
import com.arturjoshi.issues.domain.IssuePriority;
import com.arturjoshi.issues.domain.IssueResolution;
import com.arturjoshi.issues.domain.IssueStatus;
import com.arturjoshi.issues.domain.IssueType;
import com.arturjoshi.milestones.Milestone;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by ajoshi on 28-Jan-17.
 */
@NoArgsConstructor
@Data
@Entity
@EqualsAndHashCode(of = {"issueType", "issueStatus", "issuePriority", "issueResolution", "summary", "description"})
public class Issue {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssueType issueType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssueStatus issueStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssuePriority issuePriority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IssueResolution issueResolution;

    @ManyToOne
    private Account assignee;

    @ManyToOne
    private Account reporter;

    @Column(nullable = false)
    private String summary;

    private String description;

    @ManyToOne
    private Milestone milestone;
}
