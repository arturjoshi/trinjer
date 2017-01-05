package com.arturjoshi.controller;

import com.arturjoshi.authentication.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by ajoshi on 05-Jan-17.
 */
@RestController
public class CheckController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TokenAuthenticationFilter authenticationFilter;

    @RequestMapping(method = RequestMethod.GET, value = "/check")
    public String check() {
        ParameterizedTypeReference<String> ptr = new ParameterizedTypeReference<String>() {};
        HttpHeaders headers = new HttpHeaders();
        headers.set(TokenAuthenticationFilter.getAuthHeaderName(), authenticationFilter.getToken());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> result = restTemplate
                .exchange("http://localhost:8081/check", HttpMethod.GET, entity, ptr);
        return result.getBody();
    }
}
