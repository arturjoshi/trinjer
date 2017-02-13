package com.arturjoshi.project.services;

import com.arturjoshi.account.Account;
import com.arturjoshi.account.repository.AccountRepository;
import com.arturjoshi.project.Project;
import com.arturjoshi.project.controller.NotInProjectInvitationsException;
import com.arturjoshi.project.controller.ProjectIsNotVisibleException;
import com.arturjoshi.project.dto.ProjectAccountPermissionDto;
import com.arturjoshi.project.dto.ProjectAccountProfileDto;
import com.arturjoshi.project.dto.ProjectDto;
import com.arturjoshi.project.dto.ProjectInvitationDto;
import com.arturjoshi.project.entities.ProjectAccountPermission;
import com.arturjoshi.project.entities.ProjectAccountProfile;
import com.arturjoshi.project.repository.ProjectAccountPermissionRepository;
import com.arturjoshi.project.repository.ProjectAccountProfileRepository;
import com.arturjoshi.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by arturjoshi on 08-Jan-17.
 */
@Service
public class ProjectService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectAccountPermissionRepository projectAccountPermissionRepository;

    @Autowired
    private ProjectAccountProfileRepository projectAccountProfileRepository;

    public Set<Project> getAllAccountProjects(Long accountId) {
        Set<Project> projects = new HashSet<>();
        projects.addAll(accountRepository.findOne(accountId).getProjectOwned());
        projects.addAll(accountRepository.findOne(accountId).getProjects());
        return projects;
    }

    public Project createProjectForUser(ProjectDto projectDto, Long accountId) {
        Account account = accountRepository.findOne(accountId);
        Project project = projectDto.convertFromDto();
        project.setProjectOwner(account);
        projectRepository.save(project);

        projectAccountPermissionRepository.save(initProjectPermissions(account, project));
        projectAccountProfileRepository.save(initProjectProfile(account, project));
        return project;
    }

    @PreAuthorize("@projectPermissionsEvaluator.isAllowedForProject(#accountId, #projectId, 'OWNER')")
    public Project updateProject(ProjectDto projectDto, Long accountId, Long projectId) {
        Project project = projectRepository.findOne(projectId);

        project.setName(Optional.ofNullable(projectDto.getName()).orElse(project.getName()));
        project.setIsVisible(Optional.ofNullable(projectDto.getIsVisible()).orElse(project.getIsVisible()));
        return projectRepository.save(project);
    }

    @PreAuthorize("@projectPermissionsEvaluator.isAllowedForProject(#accountId, #projectId, 'OWNER')")
    public void deleteProject(Long accountId, Long projectId) {
        Project project = projectRepository.findOne(projectId);
        projectRepository.delete(project);
    }

    @PreAuthorize("@projectPermissionsEvaluator.isAllowedForProject(#accountId, #projectId, 'MASTER')")
    public Project inviteProject(ProjectInvitationDto projectInvitationDto, Long accountId, Long projectId) {
        Project project = projectRepository.findOne(projectId);
        Account invitee = accountRepository.findByEmail(projectInvitationDto.getEmail());
        return invitee == null ? inviteNewAccount(projectInvitationDto, project) :
                inviteExistingAccount(projectInvitationDto, project);
    }

    public Project joinProjectRequest(Long accountId, Long projectId) throws ProjectIsNotVisibleException {
        Project project = projectRepository.findOne(projectId);
        if (!project.getIsVisible()) throw new ProjectIsNotVisibleException();
        Account account = accountRepository.findOne(accountId);
        project.getInboxInvitations().add(account);
        return projectRepository.save(project);
    }

    public Project acceptInboxProjectInvitation(Long accountId, Long projectId) throws NotInProjectInvitationsException {
        Account account = accountRepository.findOne(accountId);
        Project project = projectRepository.findOne(projectId);
        if (!account.getProjectInvitations().contains(project)) throw new NotInProjectInvitationsException();

        project.getOutboxInvitations().remove(account);
        project.getMembers().add(account);
        return projectRepository.save(project);
    }

    public Project refuseInboxProjectInvitation(Long accountId, Long projectId) throws NotInProjectInvitationsException {
        Account account = accountRepository.findOne(accountId);
        Project project = projectRepository.findOne(projectId);
        if (!account.getProjectInvitations().contains(project)) throw new NotInProjectInvitationsException();

        project.getOutboxInvitations().remove(account);
        return projectRepository.save(project);
    }

    @PreAuthorize("@projectPermissionsEvaluator.isAllowedForProject(#ownerId, #projectId, 'OWNER')")
    public Project acceptProjectJoin(Long ownerId, Long projectId, Long accountId)
            throws NotInProjectInvitationsException {
        Project project = projectRepository.findOne(projectId);

        Account account = accountRepository.findOne(accountId);
        if (!project.getInboxInvitations().contains(account)) throw new NotInProjectInvitationsException();

        project.getInboxInvitations().remove(account);
        project.getMembers().add(account);
        return projectRepository.save(project);
    }

    @PreAuthorize("@projectPermissionsEvaluator.isAllowedForProject(#ownerId, #projectId, 'OWNER')")
    public Project refuseProjectJoin(Long ownerId, Long projectId, Long accountId)
            throws NotInProjectInvitationsException {
        Project project = projectRepository.findOne(projectId);

        Account account = accountRepository.findOne(accountId);
        if (!project.getInboxInvitations().contains(account)) throw new NotInProjectInvitationsException();

        project.getInboxInvitations().remove(account);
        return projectRepository.save(project);
    }

    @PreAuthorize("@projectPermissionsEvaluator.isAllowedForProject(#ownerId, #projectId, 'OWNER')")
    public Project kickFromProject(Long ownerId, Long projectId, Long accountId)
            throws NotInProjectInvitationsException {
        Project project = projectRepository.findOne(projectId);
        Account account = accountRepository.findOne(accountId);

        project.getMembers().remove(account);
        return projectRepository.save(project);
    }

    public List<ProjectAccountPermissionDto> findProjectAccountPermissionsByProject(Long projectId) {
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

    public List<ProjectAccountProfileDto> findProjectAccountProfilesByProject(Long projectId) {
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

    private Project inviteNewAccount(ProjectInvitationDto projectInvitationDto, Project project) {
        Account account = new Account();
        account.setEmail(projectInvitationDto.getEmail());
        account.setIsTemp(true);
        accountRepository.save(account);
        project.getOutboxInvitations().add(account);
        projectRepository.save(project);

        projectAccountPermissionRepository.save(initProjectPermissions(account, project, projectInvitationDto.getPermission()));
        projectAccountProfileRepository.save(initProjectProfile(account, project, projectInvitationDto.getProfile()));

        return project;
    }

    private Project inviteExistingAccount(ProjectInvitationDto projectInvitationDto, Project project) {
        Account account = accountRepository.findByEmail(projectInvitationDto.getEmail());
        project.getOutboxInvitations().add(account);
        projectRepository.save(project);

        projectAccountPermissionRepository.save(initProjectPermissions(account, project, projectInvitationDto.getPermission()));
        projectAccountProfileRepository.save(initProjectProfile(account, project, projectInvitationDto.getProfile()));

        return project;
    }

    private ProjectAccountPermission initProjectPermissions(Account owner, Project project) {
        return initProjectPermissions(owner, project, ProjectAccountPermission.ProjectPermission.OWNER);
    }

    private ProjectAccountProfile initProjectProfile(Account owner, Project project) {
        return initProjectProfile(owner, project, ProjectAccountProfile.ProjectProfile.MANAGER);
    }

    private ProjectAccountPermission initProjectPermissions(Account account, Project project,
                                                            ProjectAccountPermission.ProjectPermission projectPermission) {
        ProjectAccountPermission permission = new ProjectAccountPermission();
        permission.setAccount(account);
        permission.setProject(project);
        permission.setProjectPermission(Optional.ofNullable(projectPermission)
                .orElse(ProjectAccountPermission.ProjectPermission.MEMBER));
        return permission;
    }

    private ProjectAccountProfile initProjectProfile(Account owner, Project project,
                                                     ProjectAccountProfile.ProjectProfile projectProfile) {
        ProjectAccountProfile profile = new ProjectAccountProfile();
        profile.setAccount(owner);
        profile.setProject(project);
        profile.setProjectProfile(Optional.ofNullable(projectProfile).orElse(ProjectAccountProfile.ProjectProfile.DEVELOPER));
        return profile;
    }
}
