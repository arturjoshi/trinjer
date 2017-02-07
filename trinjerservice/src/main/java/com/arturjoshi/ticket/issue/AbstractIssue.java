package com.arturjoshi.ticket.issue;

import com.arturjoshi.account.Account;
import com.arturjoshi.project.Project;
import com.arturjoshi.sprint.Sprint;
import com.arturjoshi.ticket.AbstractTicket;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ajoshi on 07-Feb-17.
 */
@NoArgsConstructor
@Data
@Entity
@Table(name = "issue")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "issue_type", discriminatorType = DiscriminatorType.STRING)
public abstract class AbstractIssue extends AbstractTicket {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    private Account reporter;

    @ManyToOne
    private Account assignee;

    @OneToMany(mappedBy = "issue")
    private Set<IssueHistoryItem> issueHistory = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private IssueResolution resolution;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Sprint sprint;


}
