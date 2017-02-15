package com.arturjoshi.sprint.controller;

import com.arturjoshi.sprint.Sprint;
import com.arturjoshi.sprint.service.SprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ajoshi on 16-Jan-17.
 */
@RestController
@RequestMapping("/api")
public class SprintController {

    @Autowired
    private SprintService sprintService;

    @RequestMapping(method = RequestMethod.POST, value = "/{accountId}/createSprint/{projectId}")
    public Sprint createSprint(@PathVariable Long accountId, @PathVariable Long projectId,
                                  @RequestBody Sprint sprint) throws NotMemberProject {
        return sprintService.createSprint(accountId, projectId, sprint);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = NotMemberProject.class)
    public String handleBaseException(NotMemberProject e) {
        return SprintControllerConstants.NOT_MEMBER_PROJECT;
    }

    public static class SprintControllerConstants {
        static String NOT_MEMBER_PROJECT = "Not a project owner or member";
    }
}
