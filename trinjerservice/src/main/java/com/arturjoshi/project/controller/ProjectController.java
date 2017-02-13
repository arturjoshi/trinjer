package com.arturjoshi.project.controller;

import com.arturjoshi.project.Project;
import com.arturjoshi.project.dto.ProjectAccountPermissionDto;
import com.arturjoshi.project.dto.ProjectAccountProfileDto;
import com.arturjoshi.project.dto.ProjectDto;
import com.arturjoshi.project.dto.ProjectInvitationDto;
import com.arturjoshi.project.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Created by arturjoshi on 09-Jan-17.
 */
@RestController
@RequestMapping(value = "/api")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @RequestMapping(method = RequestMethod.GET, value = "/{accountId}/projects")
    public Set<Project> getAllAccountProjects(@PathVariable Long accountId) {
        return projectService.getAllAccountProjects(accountId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{accountId}/createProject")
    public Project createProjectForUser(@RequestBody ProjectDto projectDto, @PathVariable Long accountId) {
        return projectService.createProjectForUser(projectDto, accountId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{accountId}/updateProject/{projectId}")
    public Project updateProject(@RequestBody ProjectDto projectDto, @PathVariable Long accountId, @PathVariable Long projectId)
            throws NotOwnedProjectException {
        return projectService.updateProject(projectDto, accountId, projectId);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{accountId}/deleteProject/{projectId}")
    public void deleteProject(@PathVariable Long accountId, @PathVariable Long projectId)
            throws NotOwnedProjectException {
        projectService.deleteProject(accountId, projectId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{accountId}/inviteProjectEmail/{projectId}")
    public Project inviteProject(@RequestBody ProjectInvitationDto projectInvitationDto, @PathVariable Long accountId, @PathVariable Long projectId)
            throws NotOwnedProjectException {
        return projectService.inviteProject(projectInvitationDto, accountId, projectId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{accountId}/joinProject/{projectId}")
    public Project joinProjectRequest(@PathVariable Long accountId, @PathVariable Long projectId)
            throws ProjectIsNotVisibleException {
        return projectService.joinProjectRequest(accountId, projectId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{accountId}/confirmProjectInvitation/{projectId}")
    public Project acceptInboxProjectInvitation(@PathVariable Long accountId, @PathVariable Long projectId) throws NotInProjectInvitationsException {
        return projectService.acceptInboxProjectInvitation(accountId, projectId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{accountId}/refuseProjectInvitation/{projectId}")
    public Project refuseInboxProjectInvitation(@PathVariable Long accountId, @PathVariable Long projectId) throws NotInProjectInvitationsException {
        return projectService.refuseInboxProjectInvitation(accountId, projectId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{ownerId}/confirmProjectInboxInvitation/{projectId}/{accountId}")
    public Project acceptProjectJoin(@PathVariable Long ownerId, @PathVariable Long projectId, @PathVariable Long accountId)
            throws NotOwnedProjectException, NotInProjectInvitationsException {
        return projectService.acceptProjectJoin(ownerId, projectId, accountId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{ownerId}/refuseProjectInboxInvitation/{projectId}/{accountId}")
    public Project refuseProjectJoin(@PathVariable Long ownerId, @PathVariable Long projectId, @PathVariable Long accountId)
            throws NotOwnedProjectException, NotInProjectInvitationsException {
        return projectService.refuseProjectJoin(ownerId, projectId, accountId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{ownerId}/kickFromProject/{projectId}/{accountId}")
    public Project kickFromProject(@PathVariable Long ownerId, @PathVariable Long projectId, @PathVariable Long accountId)
            throws NotOwnedProjectException, NotInProjectInvitationsException {
        return projectService.kickFromProject(ownerId, projectId, accountId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/projectAccountPermissions/search/findByProjectId")
    public List<ProjectAccountPermissionDto> findProjectAccountPermissionsByProject(@RequestParam Long projectId) {
        return projectService.findProjectAccountPermissionsByProject(projectId);
    }

    @RequestMapping(method = RequestMethod.GET,
            value = "/projectAccountProfiles/search/findByProjectId")
    public List<ProjectAccountProfileDto> findProjectAccountProfilesByProject(@RequestParam Long projectId) {
        return projectService.findProjectAccountProfilesByProject(projectId);
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
