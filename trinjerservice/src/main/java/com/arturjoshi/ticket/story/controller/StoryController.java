package com.arturjoshi.ticket.story.controller;

import com.arturjoshi.sprint.controller.NotMemberProject;
import com.arturjoshi.ticket.story.AbstractStory;
import com.arturjoshi.ticket.story.Story;
import com.arturjoshi.ticket.story.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ajoshi on 08-Feb-17.
 */
@RestController
@RequestMapping("/api")
public class StoryController {

    @Autowired
    private StoryService storyService;

    @RequestMapping(method = RequestMethod.POST, value = "/{accountId}/project/{projectId}/createStory")
    public Story createProjectStory(@PathVariable Long accountId, @PathVariable Long projectId, @RequestBody Story story)
            throws NotMemberProject {
        return storyService.createProjectStory(accountId, projectId, story);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{accountId}/sprint/{sprintId}/createStory")
    public Story createSprintStory(@PathVariable Long accountId, @PathVariable Long sprintId, @RequestBody Story story)
            throws NotMemberProject {
        return storyService.createSprintStory(accountId, sprintId, story);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{accountId}/project/{projectId}/moveStory/{storyId}")
    public AbstractStory moveToProject(@PathVariable Long accountId, @PathVariable Long projectId, @PathVariable Long storyId) {
        return storyService.moveToProject(accountId, projectId, storyId);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{accountId}/sprint/{sprintId}/moveStory/{storyId}")
    public AbstractStory moveToSprint(@PathVariable Long accountId, @PathVariable Long sprintId, @PathVariable Long storyId) {
        return storyService.moveToSprint(accountId, sprintId, storyId);
    }

}
