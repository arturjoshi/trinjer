package com.arturjoshi.authentication.controllers;

import com.arturjoshi.account.repository.AccountRepository;
import com.arturjoshi.authentication.AccountDetails;
import com.arturjoshi.authentication.dto.AccountRegistrationDto;
import com.arturjoshi.authentication.token.TokenHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Created by arturjoshi on 04-Jan-17.
 */
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

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    @ResponseStatus(HttpStatus.OK)
    public void registerAccount(@RequestBody AccountRegistrationDto accountRegistrationDto) {
        accountRepository.save(accountRegistrationDto.getAccountFromDto());
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String authenticate(@RequestParam String username, @RequestParam String password)
            throws BadCredentialsException {

        password = passwordEncoder.encodePassword(password, null);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        return tokenHandler.createTokenForUser((AccountDetails) authentication.getPrincipal());
    }
}