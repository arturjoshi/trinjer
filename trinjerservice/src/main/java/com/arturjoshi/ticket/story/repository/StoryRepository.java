package com.arturjoshi.ticket.story.repository;

import com.arturjoshi.ticket.story.AbstractStory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by ajoshi on 07-Feb-17.
 */
@RepositoryRestResource(path = "stories")
public interface StoryRepository extends CrudRepository<AbstractStory, Long> {
}
