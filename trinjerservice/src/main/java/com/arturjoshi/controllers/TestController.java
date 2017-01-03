package com.arturjoshi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ajoshi on 02-Jan-17.
 */
@Controller
public class TestController {

    @RequestMapping(method = RequestMethod.GET, value = "/hello")
    @ResponseBody
    public String greetings() {
        return "Hello";
    }
}
