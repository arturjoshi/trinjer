package com.arturjoshi.ticket.story.service;

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

import java.util.Optional;

/**
 * Created by ajoshi on 09-Feb-17.
 */
@Service
public class StoryService {

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

    @PreAuthorize("@projectPermissionsEvaluator.isAllowedForStory(#accountId, #storyId, 'MASTER')")
    public AbstractStory updateStory(Long accountId, Long storyId, Story story) {
        AbstractStory oldStory = storyRepository.findOne(storyId);

        oldStory.setSummary(Optional.ofNullable(story.getSummary()).orElse(oldStory.getSummary()));
        oldStory.setDescription(Optional.ofNullable(story.getDescription()).orElse(oldStory.getDescription()));
        oldStory.setPriority(Optional.ofNullable(story.getPriority()).orElse(oldStory.getPriority()));
        oldStory.setStatus(Optional.ofNullable(story.getStatus()).orElse(oldStory.getStatus()));
        oldStory.setEstimate(Optional.ofNullable(story.getEstimate()).orElse(oldStory.getEstimate()));
        oldStory.setAcceptanceCriteria(Optional.ofNullable(story.getAcceptanceCriteria())
                .orElse(oldStory.getAcceptanceCriteria()));

        return storyRepository.save(oldStory);
    }

    @PreAuthorize("@projectPermissionsEvaluator.isAllowedForStory(#accountId, #storyId, 'MASTER')")
    public void deleteStory(Long accountId, Long storyId) {
        storyRepository.delete(storyRepository.findOne(storyId));
    }
}
