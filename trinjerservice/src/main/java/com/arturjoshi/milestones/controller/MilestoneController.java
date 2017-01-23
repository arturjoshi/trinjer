package com.arturjoshi.milestones.controller;

import com.arturjoshi.account.Account;
import com.arturjoshi.account.repository.AccountRepository;
import com.arturjoshi.milestones.Milestone;
import com.arturjoshi.milestones.repository.MilestoneRepository;
import com.arturjoshi.milestones.service.MilestoneService;
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
public class MilestoneController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MilestoneRepository milestoneRepository;

    @Autowired
    private MilestoneService milestoneService;

    @RequestMapping(method = RequestMethod.POST, value = "/{accountId}/createMilestone/{projectId}")
    public Milestone createMilestone(@PathVariable Long accountId, @PathVariable Long projectId,
                                     @RequestBody Milestone milestone) throws NotMemberProject {
        Account account = accountRepository.findOne(accountId);
        Project project = projectRepository.findOne(projectId);
        if(!project.getProjectOwner().equals(account) && !(project.getMembers().contains(account)))
            throw new NotMemberProject();

        milestone.setProject(project);
        return milestoneRepository.save(milestone);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{accountId}/addChildMilestone/{projectId}/{milestoneId}")
    public Milestone addChildMilestone(@PathVariable Long accountId, @PathVariable Long projectId,
                                       @PathVariable Long milestoneId, @RequestBody Milestone childMilestone) throws NotMemberProject {
        Account account = accountRepository.findOne(accountId);
        Project project = projectRepository.findOne(projectId);
        if(!project.getProjectOwner().equals(account) && !(project.getMembers().contains(account)))
            throw new NotMemberProject();

        Milestone parentMilestone = milestoneRepository.findOne(milestoneId);
        milestoneService.addChildMilestone(parentMilestone, childMilestone);
        childMilestone.setProject(project);
        milestoneRepository.save(childMilestone);
        return milestoneRepository.save(parentMilestone);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = NotMemberProject.class)
    public String handleBaseException(NotMemberProject e) {
        return MilestoneControllerConstants.NOT_MEMBER_PROJECT;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public String handleBaseException(IllegalArgumentException e) {
        return MilestoneControllerConstants.MILESTONE_CANTAINS_SPRINT;
    }

    public static class MilestoneControllerConstants {
        static String NOT_MEMBER_PROJECT = "Not a project owner or member";
        static String MILESTONE_CANTAINS_SPRINT = "Milestone may only contain sprints";
    }
}
