package com.arturjoshi.project.services;

import com.arturjoshi.account.Account;
import com.arturjoshi.account.repository.AccountRepository;
import com.arturjoshi.project.Project;
import com.arturjoshi.project.controller.NotInProjectInvitationsException;
import com.arturjoshi.project.controller.ProjectIsNotVisibleException;
import com.arturjoshi.project.dto.ProjectAccountPermissionDto;
import com.arturjoshi.project.dto.ProjectAccountProfileDto;
import com.arturjoshi.project.dto.ProjectDto;
import com.arturjoshi.project.entities.ProjectAccountPermission;
import com.arturjoshi.project.entities.ProjectAccountProfile;
import com.arturjoshi.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arturjoshi on 08-Jan-17.
 */
@Service
public class ProjectService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public Project updateProject(ProjectDto projectDto, Long accountId, Long projectId) {
        Project project = projectRepository.findOne(projectId);

        project.setName(projectDto.getName());
        project.setIsVisible(projectDto.getIsVisible());
        return projectRepository.save(project);
    }

    public void deleteProject(Long accountId, Long projectId) {
        Project project = projectRepository.findOne(projectId);
        projectRepository.delete(project);
    }

    public Project inviteProject(String email, Long accountId, Long projectId) {
        Project project = projectRepository.findOne(projectId);
        Account invitee = accountRepository.findByEmail(email);
        return invitee == null ? inviteNewAccount(email, project) :
                inviteExistingAccount(invitee, project);
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
    public Project acceptProjectJoin(Long ownerId, Long projectId, Long accountId)
            throws NotInProjectInvitationsException {
        Project project = projectRepository.findOne(projectId);

        Account account = accountRepository.findOne(accountId);
        if (!project.getInboxInvitations().contains(account)) throw new NotInProjectInvitationsException();

        project.getInboxInvitations().remove(account);
        project.getMembers().add(account);
        return projectRepository.save(project);
    }

    public Project refuseProjectJoin(Long ownerId, Long projectId, Long accountId)
            throws NotInProjectInvitationsException {
        Project project = projectRepository.findOne(projectId);

        Account account = accountRepository.findOne(accountId);
        if (!project.getInboxInvitations().contains(account)) throw new NotInProjectInvitationsException();

        project.getInboxInvitations().remove(account);
        return projectRepository.save(project);
    }

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

    private Project inviteNewAccount(String email, Project project) {
        Account account = new Account();
        account.setEmail(email);
        account.setIsTemp(true);
        accountRepository.save(account);
        project.getOutboxInvitations().add(account);
        return projectRepository.save(project);
    }

    private Project inviteExistingAccount(Account account, Project project) {
        project.getOutboxInvitations().add(account);
        return projectRepository.save(project);
    }

    public ProjectAccountPermission initDefaultProjectOwnerPermissions(Account owner, Project project) {
        ProjectAccountPermission permission = new ProjectAccountPermission();
        permission.setAccount(owner);
        permission.setProject(project);
        permission.setProjectPermission(ProjectAccountPermission.ProjectPermission.OWNER);
        return permission;
    }

    public ProjectAccountProfile initDefaultProjectOwnerProfile(Account owner, Project project) {
        ProjectAccountProfile profile = new ProjectAccountProfile();
        profile.setAccount(owner);
        profile.setProject(project);
        profile.setProjectProfile(ProjectAccountProfile.ProjectProfile.MANAGER);
        return profile;
    }
}
