package com.arturjoshi.ticket.issue;

import com.arturjoshi.account.Account;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by ajoshi on 07-Feb-17.
 */
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = {"dateTime", "assignee"})
@Entity
public class IssueHistoryItem {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    private AbstractIssue issue;

    private LocalDateTime dateTime;

    @ManyToOne
    private Account assignee;
}
