package com.arturjoshi.ticket.issue.service;

import com.arturjoshi.account.Account;
import com.arturjoshi.account.repository.AccountRepository;
import com.arturjoshi.project.Project;
import com.arturjoshi.sprint.Sprint;
import com.arturjoshi.ticket.issue.AbstractIssue;
import com.arturjoshi.ticket.issue.dto.IssueDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by arturjoshi on 17-Feb-17.
 */
@Service
public class IssueDtoConverterFactory {

    @Autowired
    private AccountRepository accountRepository;

    public <T extends AbstractIssue> IssueDtoConverter getIssueDtoConverter() {
        return new IssueDtoConverter<T>() {
            @Override
            public T convertIssueFromDto(IssueDto issueDto, Account reporter, Project project, Sprint sprint, T issue) {
                Optional.ofNullable(issueDto.getSummary()).ifPresent(issue::setSummary);
                Optional.ofNullable(issueDto.getDescription()).ifPresent(issue::setDescription);
                Optional.ofNullable(issueDto.getPriority()).ifPresent(issue::setPriority);
                Optional.ofNullable(issueDto.getStatus()).ifPresent(issue::setStatus);
                Optional.ofNullable(issueDto.getResolution()).ifPresent(issue::setResolution);
                Optional.ofNullable(issueDto.getAssigneeId()).ifPresent(assigneeId ->
                        Optional.ofNullable(accountRepository.findOne(assigneeId)).ifPresent(issue::setAssignee));
                Optional.ofNullable(reporter).ifPresent(issue::setReporter);
                Optional.ofNullable(project).ifPresent(issue::setProject);
                Optional.ofNullable(sprint).ifPresent(issue::setSprint);
                return issue;
            }
        };
    }
}
