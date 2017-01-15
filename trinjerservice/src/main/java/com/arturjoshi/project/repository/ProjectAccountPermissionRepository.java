package com.arturjoshi.project.repository;

import com.arturjoshi.project.entities.ProjectAccountPermission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ajoshi on 13-Jan-17.
 */
@Repository
public interface ProjectAccountPermissionRepository extends CrudRepository<ProjectAccountPermission, Long> {
}
