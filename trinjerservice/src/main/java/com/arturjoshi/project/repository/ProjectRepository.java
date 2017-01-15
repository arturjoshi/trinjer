package com.arturjoshi.project.repository;

import com.arturjoshi.project.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Collection;
import java.util.List;

/**
 * Created by arturjoshi on 08-Jan-17.
 */
@RepositoryRestResource(path = "projects")
public interface ProjectRepository extends CrudRepository<Project, Long> {

    @Override
    @RestResource(exported = false)
    <S extends Project> S save(S s);

    @Override
    @RestResource(exported = false)
    <S extends Project> Iterable<S> save(Iterable<S> iterable);

    @Override
    @RestResource(exported = false)
    void delete(Long aLong);

    @Override
    @RestResource(exported = false)
    void delete(Project project);

    @Override
    @RestResource(exported = false)
    void delete(Iterable<? extends Project> iterable);

    @Override
    @RestResource(exported = false)
    void deleteAll();

    Collection<Project> findByProjectOwnerEmail(String email);

    Collection<Project> findByProjectOwnerUsername(String username);

    Collection<Project> findByProjectOwnerId(Long id);

    @Query(nativeQuery = true,
            value = "select project.name, account.username, project_account_permission.project_permission " +
                    "from project_account_permission " +
                    "inner join account " +
                    "on project_account_permission.account_id = account.id " +
                    "inner join project " +
                    "on project_account_permission.project_id = project.id " +
                    "where project.id = ?1"
    )
    List<Object[]> findPermissionsByProjectId(@Param("projectId") Long projectId);

    @Query(nativeQuery = true,
            value = "select project.name, account.username, project_account_profile.project_profile " +
                    "from project_account_profile " +
                    "inner join account " +
                    "on project_account_profile.account_id = account.id " +
                    "inner join project " +
                    "on project_account_profile.project_id = project.id " +
                    "where project.id = ?1"
    )
    List<Object[]> findProfilesByProjectId(@Param("projectId") Long projectId);
}
