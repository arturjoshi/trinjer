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
            public T convertFromDto(IssueDto issueDto, Account reporter, Project project, Sprint sprint, T temp) {
                Optional.ofNullable(issueDto.getSummary()).ifPresent(temp::setSummary);
                Optional.ofNullable(issueDto.getDescription()).ifPresent(temp::setDescription);
                Optional.ofNullable(issueDto.getPriority()).ifPresent(temp::setPriority);
                Optional.ofNullable(issueDto.getStatus()).ifPresent(temp::setStatus);
                Optional.ofNullable(issueDto.getResolution()).ifPresent(temp::setResolution);
                Optional.ofNullable(issueDto.getAssigneeId()).ifPresent(assigneeId ->
                        Optional.ofNullable(accountRepository.findOne(assigneeId)).ifPresent(temp::setAssignee));
                Optional.ofNullable(reporter).ifPresent(temp::setReporter);
                Optional.ofNullable(project).ifPresent(temp::setProject);
                Optional.ofNullable(sprint).ifPresent(temp::setSprint);
                return temp;
            }
        };
    }
}
