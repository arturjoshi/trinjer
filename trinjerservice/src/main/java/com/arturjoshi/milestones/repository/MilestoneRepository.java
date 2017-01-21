package com.arturjoshi.milestones.repository;

import com.arturjoshi.milestones.Milestone;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Created by ajoshi on 16-Jan-17.
 */
@RepositoryRestResource(path = "milestones")
public interface MilestoneRepository extends CrudRepository<Milestone, Long> {

    @Override
    @RestResource(exported = false)
    <S extends Milestone> S save(S s);

    @Override
    @RestResource(exported = false)
    <S extends Milestone> Iterable<S> save(Iterable<S> iterable);

    @Override
    @RestResource(exported = false)
    void delete(Long aLong);

    @Override
    @RestResource(exported = false)
    void delete(Milestone milestone);

    @Override
    @RestResource(exported = false)
    void delete(Iterable<? extends Milestone> iterable);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
