package com.arturjoshi.account.controllers;

import com.arturjoshi.account.Account;
import com.arturjoshi.account.repository.AccountRepository;
import com.arturjoshi.account.services.AccountService;
import com.arturjoshi.project.Project;
import com.arturjoshi.project.dto.ProjectDto;
import com.arturjoshi.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by arturjoshi on 08-Jan-17.
 */
@ControllerAdvice
@RestController
@RequestMapping(value = "/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.POST, value = "/{accountId}/inviteProjectEmail/{projectId}")
    public Project inviteProject(@RequestParam String email, @PathVariable Long accountId, @PathVariable Long projectId)
            throws NotOwnedProjectException {
        Account owner = accountRepository.findOne(accountId);
        Project project = projectRepository.findOne(projectId);
        if(!project.getProjectOwner().equals(owner)) throw new NotOwnedProjectException();
        Account invitee = accountRepository.findByEmail(email);
        return invitee == null ? accountService.inviteNewAccount(email, project) :
                accountService.inviteExistingAccount(invitee, project);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{accountId}/createProject")
    public Project createProjectForUser(@RequestBody ProjectDto projectDto, @PathVariable Long accountId) {
        Account account = accountRepository.findOne(accountId);
        Project project = projectDto.convertFromDto();
        project.setProjectOwner(account);
        account.getProjectOwned().add(project);
        accountRepository.save(account);
        return projectRepository.save(project);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = NotOwnedProjectException.class)
    public String handleBaseException(NotOwnedProjectException e){
        return "It's not this users project";
    }
}
