package com.arturjoshi.ticket.issue.service;

import com.arturjoshi.account.Account;
import com.arturjoshi.account.repository.AccountRepository;
import com.arturjoshi.project.Project;
import com.arturjoshi.project.repository.ProjectRepository;
import com.arturjoshi.sprint.Sprint;
import com.arturjoshi.sprint.repository.SprintRepository;
import com.arturjoshi.ticket.issue.AbstractIssue;
import com.arturjoshi.ticket.issue.Bug;
import com.arturjoshi.ticket.issue.Improvement;
import com.arturjoshi.ticket.issue.Task;
import com.arturjoshi.ticket.issue.dto.IssueDto;
import com.arturjoshi.ticket.issue.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by arturjoshi on 17-Feb-17.
 */
@Service
public class IssueService {

    private final String BUG = "bug";
    private final String IMPROVEMENT = "improvement";
    private final String TASK = "task";

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private IssueDtoConverterFactory issueDtoConverterFactory;

    @PreAuthorize("@projectPermissionsEvaluator.isAllowedForProject(#accountId, #projectId, 'REPORTER')")
    public AbstractIssue createIssue(Long accountId, Long projectId, Long sprintId, IssueDto issueDto, String issueType) {

        Project project = projectRepository.findOne(projectId);
        Sprint sprint = sprintRepository.findOne(sprintId);
        Account reporter = accountRepository.findOne(accountId);

        if(issueType.equalsIgnoreCase(BUG)) return issueRepository.save(createBugFromDto(issueDto, reporter, project, sprint));
        if(issueType.equalsIgnoreCase(TASK)) return issueRepository.save(createTaskFromDto(issueDto, reporter, project, sprint));
        if(issueType.equalsIgnoreCase(IMPROVEMENT)) return issueRepository.save(
                createImprovementFromDto(issueDto, reporter, project, sprint));
        return null;
    }

    @PreAuthorize("@projectPermissionsEvaluator.isAllowedForProject(#accountId, #projectId, 'REPORTER')")
    public AbstractIssue updateIssue(Long accountId, Long issueId, Long projectId, IssueDto issueDto, String issueType) {
        if (issueType.equalsIgnoreCase(BUG)) {
            Bug bug = (Bug) issueRepository.findOne(issueId);
            return issueRepository.save(updateBugFromDto(issueDto, bug));
        }
        if (issueType.equalsIgnoreCase(TASK)) {
            Task task = (Task) issueRepository.findOne(issueId);
            return issueRepository.save(updateTaskFromDto(issueDto, task));
        }
        if (issueType.equalsIgnoreCase(IMPROVEMENT)) {
            Improvement improvement = (Improvement) issueRepository.findOne(issueId);
            return issueRepository.save(updateImprovementFromDto(issueDto, improvement));
        }
        return null;
    }

    private Bug createBugFromDto(IssueDto issueDto, Account reporter, Project project, Sprint sprint) {
        Bug bug = new Bug();
        issueDtoConverterFactory.getIssueDtoConverter().convertIssueFromDto(issueDto, reporter, project, sprint, bug);
        Optional.ofNullable(issueDto.getStepsToReproduce()).ifPresent(bug::setStepsToReproduce);
        return bug;
    }

    private Task createTaskFromDto(IssueDto issueDto, Account reporter, Project project, Sprint sprint) {
        Task task = new Task();
        issueDtoConverterFactory.getIssueDtoConverter().convertIssueFromDto(issueDto, reporter, project, sprint, task);
        return task;
    }

    private Improvement createImprovementFromDto(IssueDto issueDto, Account reporter, Project project, Sprint sprint) {
        Improvement improvement = new Improvement();
        issueDtoConverterFactory.getIssueDtoConverter().convertIssueFromDto(issueDto, reporter, project, sprint, improvement);
        return improvement;
    }

    private Bug updateBugFromDto(IssueDto issueDto, Bug bug) {
        issueDtoConverterFactory.getIssueDtoConverter().convertIssueFromDto(
                issueDto, null, null, null, bug);
        Optional.ofNullable(issueDto.getStepsToReproduce()).ifPresent(bug::setStepsToReproduce);
        return bug;
    }

    private Task updateTaskFromDto(IssueDto issueDto, Task task) {
        issueDtoConverterFactory.getIssueDtoConverter().convertIssueFromDto(
                issueDto, null, null, null, task);
        return task;
    }

    private Improvement updateImprovementFromDto(IssueDto issueDto, Improvement improvement) {
        issueDtoConverterFactory.getIssueDtoConverter().convertIssueFromDto(
                issueDto, null, null, null, improvement);
        return improvement;
    }
}
