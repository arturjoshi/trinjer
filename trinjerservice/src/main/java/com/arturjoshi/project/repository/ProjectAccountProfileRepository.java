package com.arturjoshi.project.repository;

import com.arturjoshi.account.Account;
import com.arturjoshi.project.entities.ProjectAccountPermission;
import com.arturjoshi.project.entities.ProjectAccountProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

/**
 * Created by ajoshi on 13-Jan-17.
 */
@Repository
public interface ProjectAccountProfileRepository extends CrudRepository<ProjectAccountProfile, Long> {

    @RestResource(exported = false)
    ProjectAccountProfile findByAccount(Account account);
}
