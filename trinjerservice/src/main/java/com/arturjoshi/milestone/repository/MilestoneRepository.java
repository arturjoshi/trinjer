package com.arturjoshi.milestone.repository;

import com.arturjoshi.milestone.Milestone;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by ajoshi on 13-Jan-17.
 */
@RepositoryRestResource(path = "milestones")
public interface MilestoneRepository extends CrudRepository<Milestone, Long> {
}
