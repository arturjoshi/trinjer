package com.arturjoshi.ticket;

import com.arturjoshi.AbstractTest;
import com.arturjoshi.authentication.dto.AccountRegistrationDto;
import com.arturjoshi.project.Project;
import com.arturjoshi.sprint.Sprint;
import com.arturjoshi.ticket.story.Story;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by ajoshi on 08-Feb-17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StoryTest extends AbstractTest {

    @Test
    public void createProjectStoryTest() throws Exception {
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

        Story story = getDefaultStory();
        String accountToken = createToken(account.getAccountFromDto());
        mockMvc.perform(post("/api/" + accountId + "/project/" + projectId + "/createStory")
                .header(X_AUTH_TOKEN_HEADER, accountToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(story)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.summary", is(STORY_SUMMARY)))
                .andExpect(jsonPath("$.description", is(STORY_DESCRIPTION)))
                .andExpect(jsonPath("$.acceptanceCriteria", is(STORY_ACCEPTANCE_CRITERIA)))
                .andExpect(jsonPath("$.estimate", is(STORY_ESTIMATE)))
                .andExpect(jsonPath("$.priority", is(STORY_PRIORITY.toString())))
                .andExpect(jsonPath("$.status", is(STORY_STATUS.toString())))
                .andExpect(jsonPath("$.project.id", is(projectId)));

        mockMvc.perform(get("/api/projects/" + projectId + "/projectBacklog"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.stories[0].summary", is(STORY_SUMMARY)))
                .andExpect(jsonPath("$._embedded.stories[0].description", is(STORY_DESCRIPTION)))
                .andExpect(jsonPath("$._embedded.stories[0].acceptanceCriteria", is(STORY_ACCEPTANCE_CRITERIA)))
                .andExpect(jsonPath("$._embedded.stories[0].estimate", is(STORY_ESTIMATE)))
                .andExpect(jsonPath("$._embedded.stories[0].priority", is(STORY_PRIORITY.toString())))
                .andExpect(jsonPath("$._embedded.stories[0].status", is(STORY_STATUS.toString())));
    }

    @Test
    public void createSprintStoryTest() throws Exception {
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

        Story story = getDefaultStory();
        mockMvc.perform(post("/api/" + accountId + "/sprint/" + sprintId + "/createStory")
                .header(X_AUTH_TOKEN_HEADER, accountToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(story)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.summary", is(STORY_SUMMARY)))
                .andExpect(jsonPath("$.description", is(STORY_DESCRIPTION)))
                .andExpect(jsonPath("$.acceptanceCriteria", is(STORY_ACCEPTANCE_CRITERIA)))
                .andExpect(jsonPath("$.estimate", is(STORY_ESTIMATE)))
                .andExpect(jsonPath("$.priority", is(STORY_PRIORITY.toString())))
                .andExpect(jsonPath("$.status", is(STORY_STATUS.toString())))
                .andExpect(jsonPath("$.sprint.id", is(sprintId)));

        mockMvc.perform(get("/api/sprints/" + sprintId + "/sprintBacklog"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.stories[0].summary", is(STORY_SUMMARY)))
                .andExpect(jsonPath("$._embedded.stories[0].description", is(STORY_DESCRIPTION)))
                .andExpect(jsonPath("$._embedded.stories[0].acceptanceCriteria", is(STORY_ACCEPTANCE_CRITERIA)))
                .andExpect(jsonPath("$._embedded.stories[0].estimate", is(STORY_ESTIMATE)))
                .andExpect(jsonPath("$._embedded.stories[0].priority", is(STORY_PRIORITY.toString())))
                .andExpect(jsonPath("$._embedded.stories[0].status", is(STORY_STATUS.toString())));
    }

    @Test
    public void moveStoryToSprintTest() throws Exception {
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

        Story story = getDefaultStory();
        String accountToken = createToken(account.getAccountFromDto());
        MvcResult storyMvcResult = mockMvc.perform(post("/api/" + accountId + "/project/" + projectId + "/createStory")
                .header(X_AUTH_TOKEN_HEADER, accountToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(story)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.summary", is(STORY_SUMMARY)))
                .andExpect(jsonPath("$.description", is(STORY_DESCRIPTION)))
                .andExpect(jsonPath("$.acceptanceCriteria", is(STORY_ACCEPTANCE_CRITERIA)))
                .andExpect(jsonPath("$.estimate", is(STORY_ESTIMATE)))
                .andExpect(jsonPath("$.priority", is(STORY_PRIORITY.toString())))
                .andExpect(jsonPath("$.status", is(STORY_STATUS.toString())))
                .andExpect(jsonPath("$.project.id", is(projectId)))
                .andReturn();

        mockMvc.perform(get("/api/projects/" + projectId + "/projectBacklog"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.stories[0].summary", is(STORY_SUMMARY)))
                .andExpect(jsonPath("$._embedded.stories[0].description", is(STORY_DESCRIPTION)))
                .andExpect(jsonPath("$._embedded.stories[0].acceptanceCriteria", is(STORY_ACCEPTANCE_CRITERIA)))
                .andExpect(jsonPath("$._embedded.stories[0].estimate", is(STORY_ESTIMATE)))
                .andExpect(jsonPath("$._embedded.stories[0].priority", is(STORY_PRIORITY.toString())))
                .andExpect(jsonPath("$._embedded.stories[0].status", is(STORY_STATUS.toString())));

        Sprint sprint = getDefaultSprint();
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

        Integer storyId = getIdFromJson(storyMvcResult.getResponse().getContentAsString());
        mockMvc.perform(patch("/api/" + accountId + "/sprint/" + sprintId + "/moveStory/" + storyId)
                .header(X_AUTH_TOKEN_HEADER, accountToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.summary", is(STORY_SUMMARY)))
                .andExpect(jsonPath("$.description", is(STORY_DESCRIPTION)))
                .andExpect(jsonPath("$.acceptanceCriteria", is(STORY_ACCEPTANCE_CRITERIA)))
                .andExpect(jsonPath("$.estimate", is(STORY_ESTIMATE)))
                .andExpect(jsonPath("$.priority", is(STORY_PRIORITY.toString())))
                .andExpect(jsonPath("$.status", is(STORY_STATUS.toString())));

        mockMvc.perform(get("/api/projects/" + projectId + "/projectBacklog"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.abstractStories.length()", is(0)));

        mockMvc.perform(get("/api/sprints/" + sprintId + "/sprintBacklog"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.stories[0].summary", is(STORY_SUMMARY)))
                .andExpect(jsonPath("$._embedded.stories[0].description", is(STORY_DESCRIPTION)))
                .andExpect(jsonPath("$._embedded.stories[0].acceptanceCriteria", is(STORY_ACCEPTANCE_CRITERIA)))
                .andExpect(jsonPath("$._embedded.stories[0].estimate", is(STORY_ESTIMATE)))
                .andExpect(jsonPath("$._embedded.stories[0].priority", is(STORY_PRIORITY.toString())))
                .andExpect(jsonPath("$._embedded.stories[0].status", is(STORY_STATUS.toString())));
    }

    @Test
    public void moveStoryToProjectTest() throws Exception {
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

        Story story = getDefaultStory();
        MvcResult storyMvcResult = mockMvc.perform(post("/api/" + accountId + "/sprint/" + sprintId + "/createStory")
                .header(X_AUTH_TOKEN_HEADER, accountToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(story)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.summary", is(STORY_SUMMARY)))
                .andExpect(jsonPath("$.description", is(STORY_DESCRIPTION)))
                .andExpect(jsonPath("$.acceptanceCriteria", is(STORY_ACCEPTANCE_CRITERIA)))
                .andExpect(jsonPath("$.estimate", is(STORY_ESTIMATE)))
                .andExpect(jsonPath("$.priority", is(STORY_PRIORITY.toString())))
                .andExpect(jsonPath("$.status", is(STORY_STATUS.toString())))
                .andExpect(jsonPath("$.sprint.id", is(sprintId)))
                .andReturn();

        mockMvc.perform(get("/api/sprints/" + sprintId + "/sprintBacklog"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.stories[0].summary", is(STORY_SUMMARY)))
                .andExpect(jsonPath("$._embedded.stories[0].description", is(STORY_DESCRIPTION)))
                .andExpect(jsonPath("$._embedded.stories[0].acceptanceCriteria", is(STORY_ACCEPTANCE_CRITERIA)))
                .andExpect(jsonPath("$._embedded.stories[0].estimate", is(STORY_ESTIMATE)))
                .andExpect(jsonPath("$._embedded.stories[0].priority", is(STORY_PRIORITY.toString())))
                .andExpect(jsonPath("$._embedded.stories[0].status", is(STORY_STATUS.toString())));

        Integer storyId = getIdFromJson(storyMvcResult.getResponse().getContentAsString());
        mockMvc.perform(patch("/api/" + accountId + "/project/" + projectId + "/moveStory/" + storyId)
                .header(X_AUTH_TOKEN_HEADER, accountToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.summary", is(STORY_SUMMARY)))
                .andExpect(jsonPath("$.description", is(STORY_DESCRIPTION)))
                .andExpect(jsonPath("$.acceptanceCriteria", is(STORY_ACCEPTANCE_CRITERIA)))
                .andExpect(jsonPath("$.estimate", is(STORY_ESTIMATE)))
                .andExpect(jsonPath("$.priority", is(STORY_PRIORITY.toString())))
                .andExpect(jsonPath("$.status", is(STORY_STATUS.toString())));

        mockMvc.perform(get("/api/sprints/" + sprintId + "/sprintBacklog"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.abstractStories.length()", is(0)));

        mockMvc.perform(get("/api/projects/" + projectId + "/projectBacklog"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.stories[0].summary", is(STORY_SUMMARY)))
                .andExpect(jsonPath("$._embedded.stories[0].description", is(STORY_DESCRIPTION)))
                .andExpect(jsonPath("$._embedded.stories[0].acceptanceCriteria", is(STORY_ACCEPTANCE_CRITERIA)))
                .andExpect(jsonPath("$._embedded.stories[0].estimate", is(STORY_ESTIMATE)))
                .andExpect(jsonPath("$._embedded.stories[0].priority", is(STORY_PRIORITY.toString())))
                .andExpect(jsonPath("$._embedded.stories[0].status", is(STORY_STATUS.toString())));
    }
}
