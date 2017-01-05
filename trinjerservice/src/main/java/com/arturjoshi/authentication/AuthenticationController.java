package com.arturjoshi.authentication;

import com.arturjoshi.account.repository.AccountRepository;
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
public class AuthenticationController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TokenHandler tokenHandler;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ShaPasswordEncoder passwordEncoder;

    @RequestMapping(method = RequestMethod.GET, value = "/securitycontext")
    @ResponseBody
    public Object getContext() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication authentication = ctx.getAuthentication();
        return authentication == null ? null : authentication.getPrincipal();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public void registerUser(@RequestBody AccountRegistrationDto accountRegistrationDto) {
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

    @RequestMapping(value = "check", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String check() {
        return "OK";
    }
}