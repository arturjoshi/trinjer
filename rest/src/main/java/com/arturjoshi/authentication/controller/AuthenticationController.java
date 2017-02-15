package com.arturjoshi.authentication.controller;

import com.arturjoshi.authentication.RequestHeaderFilter;
import com.arturjoshi.authentication.dto.AccountRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * Created by arturjoshi on 06-Jan-17.
 */
@RestController
@RequestMapping(value = "/api")
public class AuthenticationController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RequestHeaderFilter authenticationFilter;

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    @ResponseStatus(HttpStatus.OK)
    public void registerAccount(@RequestBody AccountRegistrationDto accountRegistrationDto) {
        ParameterizedTypeReference<AccountRegistrationDto> ptr = new ParameterizedTypeReference<AccountRegistrationDto>() {};
        HttpEntity<AccountRegistrationDto> entity = new HttpEntity<>(accountRegistrationDto, authenticationFilter.getHttpHeaders());
        restTemplate.exchange("http://trinjer-service/api/register", HttpMethod.POST, entity, ptr);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/authenticate")
    @ResponseStatus(HttpStatus.OK)
    public String authenticate(@RequestParam String username, @RequestParam String password) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", password);

        ParameterizedTypeReference<String> ptr = new ParameterizedTypeReference<String>() {};
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, authenticationFilter.getHttpHeaders());
        ResponseEntity<String> responseEntity = restTemplate
                .exchange("http://trinjer-service/api/authenticate", HttpMethod.POST, entity, ptr);
        return responseEntity.getBody();
    }
}
