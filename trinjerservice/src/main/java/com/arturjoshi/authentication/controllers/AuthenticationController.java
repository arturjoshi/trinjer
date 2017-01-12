package com.arturjoshi.authentication.controllers;

import com.arturjoshi.account.Account;
import com.arturjoshi.account.repository.AccountRepository;
import com.arturjoshi.authentication.AccountDetails;
import com.arturjoshi.authentication.dto.AccountRegistrationDto;
import com.arturjoshi.authentication.services.RegistrationService;
import com.arturjoshi.authentication.token.TokenHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Created by arturjoshi on 04-Jan-17.
 */
@ControllerAdvice
@RestController
@RequestMapping(value = "/api")
public class AuthenticationController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TokenHandler tokenHandler;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ShaPasswordEncoder passwordEncoder;

    @Autowired
    private RegistrationService registrationService;

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    @ResponseStatus(HttpStatus.OK)
    public Account registerAccount(@RequestBody AccountRegistrationDto accountRegistrationDto) {
        Account founded = accountRepository.findByEmail(accountRegistrationDto.getEmail());
        return founded == null ? registrationService.createNewAccount(accountRegistrationDto.getAccountFromDto()):
            registrationService.activateExistingAccount(founded, accountRegistrationDto);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public String authenticate(@RequestBody AccountRegistrationDto accountRegistrationDto)
            throws BadCredentialsException {

        String password = passwordEncoder.encodePassword(accountRegistrationDto.getPassword(), null);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(accountRegistrationDto.getUsername(), password);
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        return tokenHandler.createTokenForUser((AccountDetails) authentication.getPrincipal());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public String handleBaseException(DataIntegrityViolationException e){
        return "Account with such username or email is already exists";
    }
}