package com.arturjoshi.milestone;

import com.arturjoshi.AbstractTest;
import com.arturjoshi.authentication.dto.AccountRegistrationDto;
import com.arturjoshi.milestones.Milestone;
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
public class MilestoneTest extends AbstractTest {

    @Test
    public void createMilestoneTest() throws Exception {
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
        Milestone milestone = getDefaultMilestone(Milestone.MilestoneType.MILESTONE);

        MvcResult milestoneMvcResult = mockMvc.perform(post("/api/" + accountId + "/createMilestone/" + projectId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(milestone)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(MILESTONE_DESCRIPTION)))
                .andExpect(jsonPath("$.startDate", is(milestone.getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE))))
                .andExpect(jsonPath("$.endDate", is(milestone.getEndDate().format(DateTimeFormatter.ISO_LOCAL_DATE))))
                .andReturn();
        Integer milestoneId = getIdFromJson(milestoneMvcResult.getResponse().getContentAsString());

        mockMvc.perform(get("/api/milestones/" + milestoneId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(MILESTONE_DESCRIPTION)))
                .andExpect(jsonPath("$.startDate", is(milestone.getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE))))
                .andExpect(jsonPath("$.endDate", is(milestone.getEndDate().format(DateTimeFormatter.ISO_LOCAL_DATE))));
    }

    @Test
    public void createChildMilestoneTest() throws Exception {
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
        Milestone milestone = getDefaultMilestone(Milestone.MilestoneType.MILESTONE);

        MvcResult milestoneMvcResult = mockMvc.perform(post("/api/" + accountId + "/createMilestone/" + projectId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(milestone)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(MILESTONE_DESCRIPTION)))
                .andExpect(jsonPath("$.startDate", is(milestone.getStartDate()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE))))
                .andExpect(jsonPath("$.endDate", is(milestone.getEndDate()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE))))
                .andReturn();
        Integer milestoneId = getIdFromJson(milestoneMvcResult.getResponse().getContentAsString());

        mockMvc.perform(get("/api/milestones/" + milestoneId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(MILESTONE_DESCRIPTION)))
                .andExpect(jsonPath("$.startDate", is(milestone.getStartDate()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE))))
                .andExpect(jsonPath("$.endDate", is(milestone.getEndDate()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE))));

        Milestone sprint = getDefaultMilestone(Milestone.MilestoneType.SPRINT);

        mockMvc.perform(post("/api/" + accountId + "/addChildMilestone/" +
                projectId + "/" + milestoneId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(sprint)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(MILESTONE_DESCRIPTION)))
                .andExpect(jsonPath("$.startDate", is(milestone.getStartDate()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE))))
                .andExpect(jsonPath("$.endDate", is(milestone.getEndDate()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE))))
                .andExpect(jsonPath("$.parentMilestone.id", is(milestoneId)))
                .andExpect(jsonPath("$.parentMilestone.description", is(MILESTONE_DESCRIPTION)))
                .andExpect(jsonPath("$.parentMilestone.startDate", is(milestone.getStartDate()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE))))
                .andExpect(jsonPath("$.parentMilestone.endDate", is(milestone.getEndDate()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE))));
    }
}
