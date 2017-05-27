package com.arturjoshi.ticket;

import com.arturjoshi.AbstractTest;
import com.arturjoshi.authentication.dto.AccountRegistrationDto;
import com.arturjoshi.project.Project;
import com.arturjoshi.sprint.Sprint;
import com.arturjoshi.ticket.issue.dto.IssueDto;
import com.arturjoshi.ticket.story.Story;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.format.DateTimeFormatter;

import static com.arturjoshi.ticket.TicketPriority.TRIVIAL;
import static com.arturjoshi.ticket.TicketStatus.DONE;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by ajoshi on 08-Feb-17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IssueTest extends AbstractTest {

    @Test
    public void createBugTest() throws Exception {
        AccountRegistrationDto account = getDefaultTestAccount();
        MvcResult accountMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(account)))
                .andExpect(status().isOk())
                .andReturn();

        Integer accountId = getIdFromJson(accountMvcResult.getResponse().getContentAsString());

        Project project = getDefaultProject();
        MvcResult projectMvcResult = mockMvc.perform(post("/api/" + accountId + "/createProject")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)))
                .andReturn();

        Integer projectId = getIdFromJson(projectMvcResult.getResponse().getContentAsString());
        Sprint sprint = getDefaultSprint();

        String accountToken = createToken(account.getAccountFromDto());
        MvcResult sprintMvcResult = mockMvc.perform(post("/api/" + accountId + "/createSprint/" + projectId)
                .header(X_AUTH_TOKEN_HEADER, accountToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(sprint)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(SPRINT_DESCRIPTION)))
                .andExpect(jsonPath("$.startDate", is(sprint.getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE))))
                .andExpect(jsonPath("$.endDate", is(sprint.getEndDate().format(DateTimeFormatter.ISO_LOCAL_DATE))))
                .andReturn();

        Integer sprintId = getIdFromJson(sprintMvcResult.getResponse().getContentAsString());

        IssueDto bug = getDefaultBug();
        mockMvc.perform(post("/api/" + accountId + "/project/" + projectId + "/sprint/" + sprintId +
                "/createIssue?issueType=bug")
                .header(X_AUTH_TOKEN_HEADER, accountToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(bug)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.summary", is(ISSUE_SUMMARY)))
                .andExpect(jsonPath("$.description", is(ISSUE_DESCRIPTION)))
                .andExpect(jsonPath("$.priority", is(ISSUE_PRIORITY.toString())))
                .andExpect(jsonPath("$.status", is(ISSUE_STATUS.toString())))
                .andExpect(jsonPath("$.resolution", is(ISSUE_RESOLUTION.toString())))
                .andExpect(jsonPath("$.stepsToReproduce", is(BUG_STEPS_TO_REPRODUCE)))
                .andReturn();

        mockMvc.perform(get("/api/projects/" + projectId + "/issues"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.bugs[0].summary", is(ISSUE_SUMMARY)))
                .andExpect(jsonPath("$._embedded.bugs[0].description", is(ISSUE_DESCRIPTION)))
                .andExpect(jsonPath("$._embedded.bugs[0].priority", is(ISSUE_PRIORITY.toString())))
                .andExpect(jsonPath("$._embedded.bugs[0].status", is(ISSUE_STATUS.toString())))
                .andExpect(jsonPath("$._embedded.bugs[0].resolution", is(ISSUE_RESOLUTION.toString())))
                .andExpect(jsonPath("$._embedded.bugs[0].stepsToReproduce", is(BUG_STEPS_TO_REPRODUCE)));
    }
}
