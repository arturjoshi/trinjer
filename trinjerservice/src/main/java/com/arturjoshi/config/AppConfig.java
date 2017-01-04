package com.arturjoshi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by arturjoshi on 04-Jan-17.
 */
@Configuration
@EnableJpaRepositories("com.arturjoshi")
public class AppConfig {
}
