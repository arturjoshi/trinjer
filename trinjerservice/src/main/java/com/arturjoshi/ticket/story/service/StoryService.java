package com.arturjoshi.ticket.story.service;

import com.arturjoshi.account.repository.AccountRepository;
import com.arturjoshi.project.Project;
import com.arturjoshi.project.repository.ProjectRepository;
import com.arturjoshi.sprint.Sprint;
import com.arturjoshi.sprint.repository.SprintRepository;
import com.arturjoshi.ticket.story.AbstractStory;
import com.arturjoshi.ticket.story.Story;
import com.arturjoshi.ticket.story.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

/**
 * Created by ajoshi on 09-Feb-17.
 */
@Service
public class StoryService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private StoryRepository storyRepository;

    @PreAuthorize("@projectPermissionsEvaluator.isAllowedForProject(#accountId, #projectId, 'MASTER')")
    public Story createProjectStory(Long accountId, Long projectId, Story story) {
        Project project = projectRepository.findOne(projectId);

        story.setProject(project);
        return storyRepository.save(story);
    }

    @PreAuthorize("@projectPermissionsEvaluator.isAllowedForSprint(#accountId, #sprintId, 'MASTER')")
    public Story createSprintStory(Long accountId, Long sprintId, Story story) {
        Sprint sprint = sprintRepository.findOne(sprintId);

        story.setSprint(sprint);
        return storyRepository.save(story);
    }

    @PreAuthorize("@projectPermissionsEvaluator.isAllowedForProject(#accountId, #projectId, 'MASTER')")
    public AbstractStory moveToProject(Long accountId, Long projectId, Long storyId) {
        AbstractStory story = storyRepository.findOne(storyId);
        story.setSprint(null);
        story.setProject(projectRepository.findOne(projectId));
        return storyRepository.save(story);
    }

    @PreAuthorize("@projectPermissionsEvaluator.isAllowedForSprint(#accountId, #sprintId, 'MASTER')")
    public AbstractStory moveToSprint(Long accountId, Long sprintId, Long storyId) {
        AbstractStory story = storyRepository.findOne(storyId);
        story.setProject(null);
        story.setSprint(sprintRepository.findOne(sprintId));
        return storyRepository.save(story);
    }

}
