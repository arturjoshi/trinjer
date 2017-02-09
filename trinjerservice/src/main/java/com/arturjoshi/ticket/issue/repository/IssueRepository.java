package com.arturjoshi.ticket.issue.repository;

import com.arturjoshi.ticket.issue.AbstractIssue;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by ajoshi on 09-Feb-17.
 */
public interface IssueRepository extends CrudRepository<AbstractIssue, Long> {
}
