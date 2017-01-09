package com.arturjoshi.project.controller;

import com.arturjoshi.account.Account;
import com.arturjoshi.account.repository.AccountRepository;
import com.arturjoshi.project.services.ProjectService;
import com.arturjoshi.project.Project;
import com.arturjoshi.project.dto.ProjectAccountPermissionDto;
import com.arturjoshi.project.dto.ProjectAccountProfileDto;
import com.arturjoshi.project.dto.ProjectDto;
import com.arturjoshi.project.entities.ProjectAccountPermission;
import com.arturjoshi.project.entities.ProjectAccountProfile;
import com.arturjoshi.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arturjoshi on 09-Jan-17.
 */
@RestController
@RequestMapping(value = "/api")
public class ProjectController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    @RequestMapping(method = RequestMethod.POST, value = "/{accountId}/createProject")
    public Project createProjectForUser(@RequestBody ProjectDto projectDto, @PathVariable Long accountId) {
        Account account = accountRepository.findOne(accountId);
        Project project = projectDto.convertFromDto();
        project.setProjectOwner(account);
        account.getProjectOwned().add(project);
        accountRepository.save(account);
        return projectRepository.save(project);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{accountId}/inviteProjectEmail/{projectId}")
    public Project inviteProject(@RequestParam String email, @PathVariable Long accountId, @PathVariable Long projectId)
            throws NotOwnedProjectException {
        Account owner = accountRepository.findOne(accountId);
        Project project = projectRepository.findOne(projectId);
        if(!project.getProjectOwner().equals(owner)) throw new NotOwnedProjectException();
        Account invitee = accountRepository.findByEmail(email);
        return invitee == null ? projectService.inviteNewAccount(email, project) :
                projectService.inviteExistingAccount(invitee, project);
    }


    @RequestMapping(method = RequestMethod.GET,
            value = "/projectAccountPermissions/search/findByProjectName")
    public List<ProjectAccountPermissionDto> findProjectAccountPermissionsByProjectName(
            @RequestParam String projectName) {
        List<ProjectAccountPermissionDto> permissions = new ArrayList<>();

        for (Object[] projectAccountPermissionArray : projectRepository.findPermissionsByProjectName(projectName)) {
            permissions.add(new ProjectAccountPermissionDto(
                    projectAccountPermissionArray[0].toString(),
                    projectAccountPermissionArray[1].toString(),
                    ProjectAccountPermission.ProjectPermission.valueOf(projectAccountPermissionArray[2].toString())
            ));
        }
        return permissions;
    }

    @RequestMapping(method = RequestMethod.GET,
            value = "/projectAccountProfiles/search/findByProjectName")
    public List<ProjectAccountProfileDto> findProjectAccountProfilesByProjectName(
            @RequestParam String projectName) {
        List<ProjectAccountProfileDto> profiles = new ArrayList<>();

        for (Object[] projectAccountPermissionArray : projectRepository.findProfilesByProjectName(projectName)) {
            profiles.add(new ProjectAccountProfileDto(
                    projectAccountPermissionArray[0].toString(),
                    projectAccountPermissionArray[1].toString(),
                    ProjectAccountProfile.ProjectProfile.valueOf(projectAccountPermissionArray[2].toString())
            ));
        }
        return profiles;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = NotOwnedProjectException.class)
    public String handleBaseException(NotOwnedProjectException e){
        return "It's not this users project";
    }
}
