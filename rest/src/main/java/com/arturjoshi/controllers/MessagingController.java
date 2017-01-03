package com.arturjoshi.controllers;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ajoshi on 02-Jan-17.
 */
@Controller
public class MessagingController {
    Logger logger = Logger.getLogger(MessagingController.class);

    @Autowired
    AmqpTemplate template;

    @RequestMapping("/emit")
    @ResponseBody
    String queue1() {
        logger.info("Emit to queue1");
        template.convertAndSend("queue1","Message to queue");
        return "Emit to queue";
    }
}
