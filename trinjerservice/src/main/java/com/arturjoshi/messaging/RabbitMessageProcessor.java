package com.arturjoshi.messaging;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created by ajoshi on 02-Jan-17.
 */
@EnableRabbit
@Component
public class RabbitMessageProcessor {
    Logger logger = Logger.getLogger(RabbitMessageProcessor.class);
    @RabbitListener(queues = "queue1")
    public void processQueue1(String message) {
        logger.info("Received from queue 1: " + message);
    }
}
