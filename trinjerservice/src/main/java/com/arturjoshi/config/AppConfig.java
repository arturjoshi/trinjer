package com.arturjoshi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
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

    @Bean
    public RepositoryRestConfigurer repositoryRestConfigurer() {
        return new RepositoryRestConfigurerAdapter() {
            @Override
            public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
                config.setBasePath("/api");
            }
        };
    }

    @Bean
    @Primary
    public RepositoryRestConfiguration repositoryRestConfiguration(PersistentEntities persistentEntities,
                                                                   RepositoryRestConfiguration repositoryRestConfiguration) {
        for (PersistentEntity<?, ?> persistentEntity : persistentEntities) {
            repositoryRestConfiguration.exposeIdsFor(persistentEntity.getType());
        }
        return repositoryRestConfiguration;
    }
}
