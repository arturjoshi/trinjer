package com.arturjoshi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

/**
 * Created by arturjoshi on 04-Jan-17.
 */
@Configuration
@EnableJpaRepositories("com.arturjoshi")
public class AppConfig {

    @Bean
    public ShaPasswordEncoder passwordEncoder() {
        return new ShaPasswordEncoder(256);
    }
}
