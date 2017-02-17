package com.arturjoshi.ticket.issue.controller;

import com.arturjoshi.ticket.issue.AbstractIssue;
import com.arturjoshi.ticket.issue.dto.IssueDto;
import com.arturjoshi.ticket.issue.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by arturjoshi on 17-Feb-17.
 */
@RestController
@RequestMapping("/api")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @RequestMapping(method = RequestMethod.POST, value = "/api/{accountId}/project/{projectId}/sprint/{sprintId}")
    public AbstractIssue createIssue(@RequestBody IssueDto issueDto, @PathVariable Long accountId,
                                     @PathVariable Long projectId, @PathVariable Long sprintId,
                                     @RequestParam String issueType) {
        return issueService.createIssue(accountId, projectId, sprintId, issueDto, issueType);
    }

}
