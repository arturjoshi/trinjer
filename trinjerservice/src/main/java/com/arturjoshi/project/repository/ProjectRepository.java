package com.arturjoshi.project.repository;

import com.arturjoshi.project.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Collection;

/**
 * Created by arturjoshi on 08-Jan-17.
 */
@RestResource(path = "projects")
public interface ProjectRepository extends CrudRepository<Project, Long> {
    Collection<Project> findByProjectOwnerEmail(String email);

    Collection<Project> findByProjectOwnerUsername(String username);

    Collection<Project> findByProjectOwnerId(Long id);
}
