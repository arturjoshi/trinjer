package com.arturjoshi.project;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by arturjoshi on 07-Jan-17.
 */
@Repository
public interface ProjectRoleRepository extends CrudRepository<ProjectRole, Long> {
}
