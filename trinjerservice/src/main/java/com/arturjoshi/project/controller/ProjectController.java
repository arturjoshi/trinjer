package com.arturjoshi.project.controller;

import com.arturjoshi.account.Account;
import com.arturjoshi.account.repository.AccountRepository;
import com.arturjoshi.project.Project;
import com.arturjoshi.project.dto.ProjectAccountPermissionDto;
import com.arturjoshi.project.dto.ProjectAccountProfileDto;
import com.arturjoshi.project.dto.ProjectDto;
import com.arturjoshi.project.entities.ProjectAccountPermission;
import com.arturjoshi.project.entities.ProjectAccountProfile;
import com.arturjoshi.project.repository.ProjectAccountPermissionRepository;
import com.arturjoshi.project.repository.ProjectAccountProfileRepository;
import com.arturjoshi.project.repository.ProjectRepository;
import com.arturjoshi.project.services.ProjectService;
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

    @Autowired
    private ProjectAccountPermissionRepository projectAccountPermissionRepository;

    @Autowired
    private ProjectAccountProfileRepository projectAccountProfileRepository;

    @RequestMapping(method = RequestMethod.POST, value = "/{accountId}/createProject")
    public Project createProjectForUser(@RequestBody ProjectDto projectDto, @PathVariable Long accountId) {
        Account account = accountRepository.findOne(accountId);
        Project project = projectDto.convertFromDto();
        project.setProjectOwner(account);
        projectRepository.save(project);

        projectAccountPermissionRepository.save(projectService.initDefaultProjectOwnerPermissions(account, project));
        projectAccountProfileRepository.save(projectService.initDefaultProjectOwnerProfile(account, project));
        return project;
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

    @RequestMapping(method = RequestMethod.POST, value = "/{accountId}/joinProject/{projectId}")
    public Project joinProjectRequest(@PathVariable Long accountId, @PathVariable Long projectId)
            throws NotOwnedProjectException, ProjectIsNotVisibleException {
        Project project = projectRepository.findOne(projectId);
        if (!project.getIsVisible()) throw new ProjectIsNotVisibleException();
        Account account = accountRepository.findOne(accountId);
        project.getInboxInvitations().add(account);
        return projectRepository.save(project);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{accountId}/confirmProjectInvitation/{projectId}")
    public Project acceptInboxProjectInvitation(@PathVariable Long accountId, @PathVariable Long projectId) throws NotInProjectInvitationsException {
        Account account = accountRepository.findOne(accountId);
        Project project = projectRepository.findOne(projectId);
        if (!account.getProjectInvitations().contains(project)) throw new NotInProjectInvitationsException();

        project.getOutboxInvitations().remove(account);
        project.getMembers().add(account);
        return projectRepository.save(project);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{accountId}/refuseProjectInvitation/{projectId}")
    public Project refuseInboxProjectInvitation(@PathVariable Long accountId, @PathVariable Long projectId) throws NotInProjectInvitationsException {
        Account account = accountRepository.findOne(accountId);
        Project project = projectRepository.findOne(projectId);
        if (!account.getProjectInvitations().contains(project)) throw new NotInProjectInvitationsException();

        project.getOutboxInvitations().remove(account);
        return projectRepository.save(project);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{ownerId}/confirmProjectInboxInvitation/{projectId}/{accountId}")
    public Project acceptProjectJoin(@PathVariable Long ownerId, @PathVariable Long projectId, @PathVariable Long accountId)
            throws NotOwnedProjectException, NotInProjectInvitationsException {
        Account owner = accountRepository.findOne(ownerId);
        Project project = projectRepository.findOne(projectId);
        if (!project.getProjectOwner().equals(owner)) throw new NotOwnedProjectException();

        Account account = accountRepository.findOne(accountId);
        if (!project.getInboxInvitations().contains(account)) throw new NotInProjectInvitationsException();

        project.getInboxInvitations().remove(account);
        project.getMembers().add(account);
        return projectRepository.save(project);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{ownerId}/refuseProjectInboxInvitation/{projectId}/{accountId}")
    public Project refuseProjectJoin(@PathVariable Long ownerId, @PathVariable Long projectId, @PathVariable Long accountId)
            throws NotOwnedProjectException, NotInProjectInvitationsException {
        Account owner = accountRepository.findOne(ownerId);
        Project project = projectRepository.findOne(projectId);
        if (!project.getProjectOwner().equals(owner)) throw new NotOwnedProjectException();

        Account account = accountRepository.findOne(accountId);
        if (!project.getInboxInvitations().contains(account)) throw new NotInProjectInvitationsException();

        project.getInboxInvitations().remove(account);
        return projectRepository.save(project);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{ownerId}/kickFromProject/{projectId}/{accountId}")
    public Project kickFromProject(@PathVariable Long ownerId, @PathVariable Long projectId, @PathVariable Long accountId)
            throws NotOwnedProjectException, NotInProjectInvitationsException {
        Account owner = accountRepository.findOne(ownerId);
        Project project = projectRepository.findOne(projectId);
        if (!project.getProjectOwner().equals(owner)) throw new NotOwnedProjectException();

        Account account = accountRepository.findOne(accountId);

        project.getMembers().remove(account);
        return projectRepository.save(project);
    }

    @RequestMapping(method = RequestMethod.GET,
            value = "/projectAccountPermissions/search/findByProjectId")
    public List<ProjectAccountPermissionDto> findProjectAccountPermissionsByProjectName(
            @RequestParam Long projectId) {
        List<ProjectAccountPermissionDto> permissions = new ArrayList<>();

        for (Object[] projectAccountPermissionArray : projectRepository.findPermissionsByProjectId(projectId)) {
            permissions.add(new ProjectAccountPermissionDto(
                    projectAccountPermissionArray[0].toString(),
                    projectAccountPermissionArray[1].toString(),
                    ProjectAccountPermission.ProjectPermission.valueOf(projectAccountPermissionArray[2].toString())
            ));
        }
        return permissions;
    }

    @RequestMapping(method = RequestMethod.GET,
            value = "/projectAccountProfiles/search/findByProjectId")
    public List<ProjectAccountProfileDto> findProjectAccountProfilesByProjectName(
            @RequestParam Long projectId) {
        List<ProjectAccountProfileDto> profiles = new ArrayList<>();

        for (Object[] projectAccountPermissionArray : projectRepository.findProfilesByProjectId(projectId)) {
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
    public String handleBaseException(NotOwnedProjectException e) {
        return ProjectControllerConstants.NOT_USERS_PROJECT;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = ProjectIsNotVisibleException.class)
    public String handleBaseException(ProjectIsNotVisibleException e) {
        return ProjectControllerConstants.NOT_VISIBLE_PROJECT;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = NotInProjectInvitationsException.class)
    public String handleBaseException(NotInProjectInvitationsException e) {
        return ProjectControllerConstants.NOT_SUCH_INVITATION;
    }

    public static class ProjectControllerConstants {
        static String NOT_USERS_PROJECT = "It's not this users project";
        static String NOT_VISIBLE_PROJECT = "Project is not visible";
        static String NOT_SUCH_INVITATION = "No such project invitation";

        private ProjectControllerConstants() {
        }
    }
}
