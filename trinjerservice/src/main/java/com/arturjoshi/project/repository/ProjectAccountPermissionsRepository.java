package com.arturjoshi.project.repository;

import com.arturjoshi.project.entity.accountpermission.ProjectAccountPermission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;

/**
 * Created by arturjoshi on 09-Jan-17.
 */
@RepositoryRestResource
public interface ProjectAccountPermissionsRepository extends CrudRepository<ProjectAccountPermission, Long> {
    Collection<ProjectAccountPermission> findByProjectName(@Param("projectName") String name);
}
