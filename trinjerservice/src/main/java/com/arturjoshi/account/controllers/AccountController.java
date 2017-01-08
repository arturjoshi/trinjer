package com.arturjoshi.account.controllers;

import com.arturjoshi.account.Account;
import com.arturjoshi.account.repository.AccountRepository;
import com.arturjoshi.project.Project;
import com.arturjoshi.project.dto.ProjectDto;
import com.arturjoshi.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by arturjoshi on 08-Jan-17.
 */
@RestController
@RequestMapping(value = "/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @RequestMapping(method = RequestMethod.POST, value = "/{accountId}/createProject")
    public Project createProjectForUser(@RequestBody ProjectDto projectDto, @PathVariable Long accountId) {
        Account account = accountRepository.findOne(accountId);
        Project project = projectDto.convertFromDto();
        project.setProjectOwner(account);
        account.getProjectOwned().add(project);
        accountRepository.save(account);
        return projectRepository.save(project);
    }
}
