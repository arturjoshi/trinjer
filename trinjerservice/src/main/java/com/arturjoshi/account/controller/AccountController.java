package com.arturjoshi.account.controller;

import com.arturjoshi.account.Account;
import com.arturjoshi.account.AccountCredentials;
import com.arturjoshi.account.repository.AccountRepository;
import com.arturjoshi.authentication.dto.AccountRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by arturjoshi on 05-Feb-17.
 */
@RestController
@RequestMapping(value = "/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(method = RequestMethod.PATCH, value = "/{accountId}/updateAccount/username")
    public Account updateAccountUsername(@RequestBody AccountRegistrationDto accountDto, @PathVariable("accountId") Long accountId) {
        Account account = accountRepository.findOne(accountId);
        account.setUsername(accountDto.getUsername());
        return accountRepository.save(account);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{accountId}/updateAccount/email")
    public Account updateAccountEmail(@RequestBody AccountRegistrationDto accountDto, @PathVariable("accountId") Long accountId) {
        Account account = accountRepository.findOne(accountId);
        account.setEmail(accountDto.getEmail());
        return accountRepository.save(account);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{accountId}/updateAccount/password")
    public Account updateAccountPassword(@RequestBody AccountRegistrationDto accountDto, @PathVariable("accountId") Long accountId) {
        Account account = accountRepository.findOne(accountId);
        account.setCredentials(accountDto.getAccountFromDto().getCredentials());
        return accountRepository.save(account);
    }
}
