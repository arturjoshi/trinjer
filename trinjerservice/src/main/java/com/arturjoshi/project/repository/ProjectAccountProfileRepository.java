package com.arturjoshi.project.repository;

import com.arturjoshi.project.entities.ProjectAccountProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ajoshi on 13-Jan-17.
 */
@Repository
public interface ProjectAccountProfileRepository extends CrudRepository<ProjectAccountProfile, Long> {
}
