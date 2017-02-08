package com.arturjoshi.ticket.story.controller;

import com.arturjoshi.account.Account;
import com.arturjoshi.account.repository.AccountRepository;
import com.arturjoshi.project.Project;
import com.arturjoshi.project.repository.ProjectRepository;
import com.arturjoshi.sprint.Sprint;
import com.arturjoshi.sprint.controller.NotMemberProject;
import com.arturjoshi.sprint.repository.SprintRepository;
import com.arturjoshi.ticket.story.Story;
import com.arturjoshi.ticket.story.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ajoshi on 08-Feb-17.
 */
@RestController
@RequestMapping("/api")
public class StoryController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private StoryRepository storyRepository;

    @RequestMapping(method = RequestMethod.POST, value = "/{accountId}/project/{projectId}/createStory")
    public Story createProjectStory(@PathVariable Long accountId, @PathVariable Long projectId, @RequestBody Story story)
            throws NotMemberProject {
        Account account = accountRepository.findOne(accountId);
        Project project = projectRepository.findOne(projectId);

        if(!project.getProjectOwner().equals(account) && !project.getMembers().contains(account))
            throw new NotMemberProject();

        story.setProject(project);
        return storyRepository.save(story);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{accountId}/sprint/{sprintId}/createStory")
    public Story createSprintStory(@PathVariable Long accountId, @PathVariable Long sprintId, @RequestBody Story story)
            throws NotMemberProject {
        Account account = accountRepository.findOne(accountId);
        Sprint sprint = sprintRepository.findOne(sprintId);

        if(!sprint.getProject().getProjectOwner().equals(account) && !sprint.getProject().getMembers().contains(account))
            throw new NotMemberProject();

        story.setSprint(sprint);
        return storyRepository.save(story);
    }
}
