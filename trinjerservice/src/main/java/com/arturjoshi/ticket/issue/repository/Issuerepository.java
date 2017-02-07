package com.arturjoshi.ticket.issue.repository;

import com.arturjoshi.ticket.issue.AbstractIssue;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by ajoshi on 07-Feb-17.
 */
@RepositoryRestResource(path = "issues")
public interface IssueRepository extends CrudRepository<AbstractIssue, Long> {
}
