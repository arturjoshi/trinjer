package com.arturjoshi.config;

import com.arturjoshi.project.ProjectRole;
import com.arturjoshi.project.ProjectRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Stream;

/**
 * Created by arturjoshi on 07-Jan-17.
 */
@Configuration
@ComponentScan("com.arturjoshi.project")
public class ProjectRoleDataConfig {
    @Bean
    public CommandLineRunner commandLineRunner(ProjectRoleRepository repository) {
        return repository.count() == 0 ?
                strings -> Stream.of("OWNER", "DEVELOPER", "QA", "MANAGER")
                        .forEach(s -> repository.save(new ProjectRole(s))) : null;
    }
}
