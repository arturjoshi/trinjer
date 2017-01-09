package com.arturjoshi.project.services;

import com.arturjoshi.account.Account;
import com.arturjoshi.account.repository.AccountRepository;
import com.arturjoshi.project.Project;
import com.arturjoshi.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by arturjoshi on 08-Jan-17.
 */
@Service
public class ProjectService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public Project inviteNewAccount(String email, Project project) {
        Account account = new Account();
        account.setEmail(email);
        account.setIsTemp(true);
        accountRepository.save(account);
        project.getOutboxInvitations().add(account);
        return projectRepository.save(project);
    }

    public Project inviteExistingAccount(Account account, Project project) {
        project.getOutboxInvitations().add(account);
        return projectRepository.save(project);
    }
}
