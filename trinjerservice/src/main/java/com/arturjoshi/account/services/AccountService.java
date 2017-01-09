package com.arturjoshi.account.services;

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
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public Project inviteNewAccount(String email, Project project) {
        Account account = new Account();
        account.setEmail(email);
        account.setIsTemp(true);
        account.getProjectInvitations().add(project);
        project.getInvitations().add(account);
        accountRepository.save(account);
        return projectRepository.save(project);
    }

    public Project inviteExistingAccount(Account account, Project project) {
        account.getProjectInvitations().add(project);
        accountRepository.save(account);
        project.getInvitations().add(account);
        return projectRepository.save(project);
    }
}
