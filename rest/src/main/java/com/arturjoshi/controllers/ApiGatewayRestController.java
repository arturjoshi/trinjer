package com.arturjoshi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/**
 * Created by ajoshi on 02-Jan-17.
 */
@Controller
@RequestMapping("/api")
public class ApiGatewayRestController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(method = RequestMethod.GET, value = "/hello")
    @ResponseBody
    public String hello() {
        ParameterizedTypeReference<String> ptr = new ParameterizedTypeReference<String>() {};
        ResponseEntity<String> resultEntity = this.restTemplate.exchange("http://localhost:8080/trinjer-service/hello", HttpMethod.GET, null, ptr);
        return resultEntity.getBody();
    }
}
