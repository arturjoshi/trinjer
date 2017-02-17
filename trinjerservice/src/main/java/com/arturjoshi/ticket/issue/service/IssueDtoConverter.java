package com.arturjoshi.ticket.issue.service;

import com.arturjoshi.account.Account;
import com.arturjoshi.project.Project;
import com.arturjoshi.sprint.Sprint;
import com.arturjoshi.ticket.issue.AbstractIssue;
import com.arturjoshi.ticket.issue.dto.IssueDto;

/**
 * Created by arturjoshi on 17-Feb-17.
 */
public interface IssueDtoConverter<T extends AbstractIssue> {
    T convertFromDto(IssueDto issueDto, Account reporter, Project project, Sprint sprint, T temp);
}
