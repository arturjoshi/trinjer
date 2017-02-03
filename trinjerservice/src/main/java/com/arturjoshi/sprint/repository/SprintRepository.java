package com.arturjoshi.sprint.repository;

import com.arturjoshi.sprint.Sprint;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Created by ajoshi on 16-Jan-17.
 */
@RepositoryRestResource(path = "sprints")
public interface SprintRepository extends CrudRepository<Sprint, Long> {

    @Override
    @RestResource(exported = false)
    <S extends Sprint> S save(S s);

    @Override
    @RestResource(exported = false)
    <S extends Sprint> Iterable<S> save(Iterable<S> iterable);

    @Override
    @RestResource(exported = false)
    void delete(Long aLong);

    @Override
    @RestResource(exported = false)
    void delete(Sprint sprint);

    @Override
    @RestResource(exported = false)
    void delete(Iterable<? extends Sprint> iterable);

    @Override
    @RestResource(exported = false)
    void deleteAll();
}
