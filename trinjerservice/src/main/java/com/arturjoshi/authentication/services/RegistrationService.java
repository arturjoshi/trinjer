package com.arturjoshi.authentication.services;

import com.arturjoshi.account.Account;
import com.arturjoshi.account.repository.AccountRepository;
import com.arturjoshi.authentication.dto.AccountRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by arturjoshi on 08-Jan-17.
 */
@Service
public class RegistrationService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createNewAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account activateExistingAccount(Account account, AccountRegistrationDto accountRegistrationDto) {
        account.setUsername(accountRegistrationDto.getUsername());
        account.setIsTemp(false);
        account.setCredentials(accountRegistrationDto.getAccountFromDto().getCredentials());
        return accountRepository.save(account);
    }
}
