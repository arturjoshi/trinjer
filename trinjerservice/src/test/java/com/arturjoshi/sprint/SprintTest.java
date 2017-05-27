package com.arturjoshi.sprint;

import com.arturjoshi.AbstractTest;
import com.arturjoshi.authentication.dto.AccountRegistrationDto;
import com.arturjoshi.project.Project;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by ajoshi on 01-Feb-17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SprintTest extends AbstractTest {

    @Test
    public void createSprintTest() throws Exception {
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

        mockMvc.perform(get("/api/sprints/" + sprintId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(SPRINT_DESCRIPTION)))
                .andExpect(jsonPath("$.startDate", is(sprint.getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE))))
                .andExpect(jsonPath("$.endDate", is(sprint.getEndDate().format(DateTimeFormatter.ISO_LOCAL_DATE))));
    }

}
