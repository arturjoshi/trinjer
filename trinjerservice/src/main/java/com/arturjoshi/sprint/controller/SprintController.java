package com.arturjoshi.sprint.controller;

import com.arturjoshi.account.Account;
import com.arturjoshi.account.repository.AccountRepository;
import com.arturjoshi.sprint.Sprint;
import com.arturjoshi.sprint.repository.SprintRepository;
import com.arturjoshi.project.Project;
import com.arturjoshi.project.repository.ProjectRepository;
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
    private AccountRepository accountRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SprintRepository sprintRepository;

    @RequestMapping(method = RequestMethod.POST, value = "/{accountId}/createSprint/{projectId}")
    public Sprint createSprint(@PathVariable Long accountId, @PathVariable Long projectId,
                                  @RequestBody Sprint sprint) throws NotMemberProject {
        Account account = accountRepository.findOne(accountId);
        Project project = projectRepository.findOne(projectId);
        if(!project.getProjectOwner().equals(account) && !(project.getMembers().contains(account)))
            throw new NotMemberProject();

        sprint.setProject(project);
        return sprintRepository.save(sprint);
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
