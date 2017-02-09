package com.arturjoshi.project.services;

import com.arturjoshi.account.Account;
import com.arturjoshi.account.repository.AccountRepository;
import com.arturjoshi.project.Project;
import com.arturjoshi.project.entities.ProjectAccountPermission;
import com.arturjoshi.project.repository.ProjectRepository;
import com.arturjoshi.sprint.Sprint;
import com.arturjoshi.sprint.repository.SprintRepository;
import com.arturjoshi.ticket.story.AbstractStory;
import com.arturjoshi.ticket.story.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ajoshi on 08-Feb-17.
 */
@Component("projectPermissionsEvaluator")
public class ProjectPermissionsEvaluator {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private StoryRepository storyRepository;

    public boolean isAllowedForProject(Long accountId, Long projectId, String requiredPermission) {
        Account account = accountRepository.findOne(accountId);
        Project project = projectRepository.findOne(projectId);
        ProjectAccountPermission actualPermission = project.getProjectAccountPermissions()
                .stream()
                .filter(projectAccountPermission -> projectAccountPermission.getAccount().equals(account))
                .findFirst()
                .orElse(new ProjectAccountPermission(project, account, ProjectAccountPermission.ProjectPermission.MEMBER));
        return actualPermission.getProjectPermission().ordinal() >=
                ProjectAccountPermission.ProjectPermission.valueOf(requiredPermission).ordinal();
    }

    public boolean isAllowedForSprint(Long accountId, Long sprintId, String requiredPermission) {
        Sprint sprint = sprintRepository.findOne(sprintId);
        return isAllowedForProject(accountId, sprint.getProject().getId(), requiredPermission);
    }

    public boolean isAllowedForStory(Long accountId, Long storyId, String requiredPermission) {
        AbstractStory story = storyRepository.findOne(storyId);
        Project project = story.getProject() != null ? story.getProject() : story.getSprint().getProject();
        return isAllowedForProject(accountId, project.getId(), requiredPermission);
    }
}
