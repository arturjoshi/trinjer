package com.arturjoshi.project;

import com.arturjoshi.AbstractTest;
import com.arturjoshi.authentication.dto.AccountRegistrationDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by arturjoshi on 08-Jan-17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectTest extends AbstractTest {

    @Test
    public void createProjectTest() throws Exception {
        AccountRegistrationDto account = getDefaultTestAccount();
        MvcResult accountMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(account)))
                .andExpect(status().isOk())
                .andReturn();

        Integer accountId = getIdFromJson(accountMvcResult.getResponse().getContentAsString());

        Project project = getDefaultProject();
        mockMvc.perform(post("/api/" + accountId + "/createProject")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)));
    }

    @Test
    public void updateProjectTest() throws Exception {
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

        project.setName(PROJECT_NAME_UPDATED);
        project.setIsVisible(NON_VISIBLE_PROJECT);
        Integer projectId = getIdFromJson(projectMvcResult.getResponse().getContentAsString());

        String accountToken = createToken(account.getAccountFromDto());
        mockMvc.perform(post("/api/" + accountId + "/updateProject/" + projectId)
                .header(X_AUTH_TOKEN_HEADER, accountToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(PROJECT_NAME_UPDATED)))
                .andExpect(jsonPath("$.isVisible", is(NON_VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)));
    }

    @Test
    public void deleteProjectTest() throws Exception {
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

        String accountToken = createToken(account.getAccountFromDto());
        mockMvc.perform(delete("/api/" + accountId + "/deleteProject/" + projectId)
                .header(X_AUTH_TOKEN_HEADER, accountToken))
                .andExpect(status().isOk());
    }

    @Test
    public void inviteToProject() throws Exception {
        AccountRegistrationDto owner = getDefaultTestAccount();
        MvcResult accountMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(owner)))
                .andExpect(status().isOk())
                .andReturn();

        Integer ownerId = getIdFromJson(accountMvcResult.getResponse().getContentAsString());

        String ownerToken = createToken(owner.getAccountFromDto());
        Project project = getDefaultProject();
        MvcResult projectMvcResult = mockMvc.perform(post("/api/" + ownerId + "/createProject")
                .header(X_AUTH_TOKEN_HEADER, ownerToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)))
                .andReturn();

        Integer projectId = getIdFromJson(projectMvcResult.getResponse().getContentAsString());

        mockMvc.perform(post("/api/" + ownerId + "/inviteProjectEmail/" +
                projectId + "?email=" + (ACCOUNT_EMAIL + TMP_SUFFIX))
                .header(X_AUTH_TOKEN_HEADER, ownerToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(project)))
                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)));

        AccountRegistrationDto invitee = getDefaultTestAccount(TMP_SUFFIX);
        MvcResult applicantMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(invitee)))
                .andExpect(status().isOk())
                .andReturn();

        Integer inviteeId = getIdFromJson(applicantMvcResult.getResponse().getContentAsString());
        String inviteeToken = createToken(invitee.getAccountFromDto());
        mockMvc.perform(get("/api/accounts/" + inviteeId + "/projectInvitations")
                .header(X_AUTH_TOKEN_HEADER, inviteeToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.projects[0].id", is(projectId)))
                .andExpect(jsonPath("$._embedded.projects[0].name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$._embedded.projects[0].isVisible", is(VISIBLE_PROJECT)));
    }

    @Test
    public void joinProject() throws Exception {
        AccountRegistrationDto owner = getDefaultTestAccount();
        MvcResult ownerMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(owner)))
                .andExpect(status().isOk())
                .andReturn();

        Integer ownerId = getIdFromJson(ownerMvcResult.getResponse().getContentAsString());

        String ownerToken = createToken(owner.getAccountFromDto());
        Project project = getDefaultProject();
        MvcResult projectMvcResult = mockMvc.perform(post("/api/" + ownerId + "/createProject")
                .header(X_AUTH_TOKEN_HEADER, ownerToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)))
                .andReturn();

        Integer projectId = getIdFromJson(projectMvcResult.getResponse().getContentAsString());

        AccountRegistrationDto applicant = getDefaultTestAccount(TMP_SUFFIX);
        MvcResult applicantMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(applicant)))
                .andExpect(status().isOk())
                .andReturn();

        Integer applicantId = getIdFromJson(applicantMvcResult.getResponse().getContentAsString());

        String applicantToken = createToken(applicant.getAccountFromDto());
        mockMvc.perform(post("/api/" + applicantId + "/joinProject/" + projectId)
                .header(X_AUTH_TOKEN_HEADER, applicantToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)));

        mockMvc.perform(get("/api/projects/" + projectId + "/inboxInvitations")
                .header(X_AUTH_TOKEN_HEADER, applicantToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.accounts[0].id", is(applicantId)))
                .andExpect(jsonPath("$._embedded.accounts[0].username", is(ACCOUNT_USERNAME + TMP_SUFFIX)))
                .andExpect(jsonPath("$._embedded.accounts[0].email", is(ACCOUNT_EMAIL + TMP_SUFFIX)));
    }

    @Test
    public void ownedProject() throws Exception {
        AccountRegistrationDto account = getDefaultTestAccount();
        MvcResult accountMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(account)))
                .andExpect(status().isOk())
                .andReturn();

        Integer accountId = getIdFromJson(accountMvcResult.getResponse().getContentAsString());

        String accountToken = createToken(account.getAccountFromDto());
        Project project = getDefaultProject();
        MvcResult projectMvcResult = mockMvc.perform(post("/api/" + accountId + "/createProject")
                .header(X_AUTH_TOKEN_HEADER, accountToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)))
                .andReturn();

        Integer projectId = getIdFromJson(projectMvcResult.getResponse().getContentAsString());
        mockMvc.perform(get("/api/accounts/" + accountId + "/projectOwned")
                .header(X_AUTH_TOKEN_HEADER, accountToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.projects[0].id", is(projectId)))
                .andExpect(jsonPath("$._embedded.projects[0].name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$._embedded.projects[0].isVisible", is(VISIBLE_PROJECT)));
    }

    @Test
    public void projectOutboxInvitations() throws Exception {
        AccountRegistrationDto account = getDefaultTestAccount();
        MvcResult accountMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(account)))
                .andExpect(status().isOk())
                .andReturn();

        Integer accountId = getIdFromJson(accountMvcResult.getResponse().getContentAsString());

        String accountToken = createToken(account.getAccountFromDto());
        Project project = getDefaultProject();
        MvcResult projectMvcResult = mockMvc.perform(post("/api/" + accountId + "/createProject")
                .header(X_AUTH_TOKEN_HEADER, accountToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)))
                .andReturn();

        Integer projectId = getIdFromJson(projectMvcResult.getResponse().getContentAsString());

        mockMvc.perform(post("/api/" + accountId + "/inviteProjectEmail/" +
                projectId + "?email=" + ACCOUNT_EMAIL + TMP_SUFFIX)
                .header(X_AUTH_TOKEN_HEADER, accountToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(project)))
                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)));

        mockMvc.perform(get("/api/projects/" + projectId + "/outboxInvitations")
                .header(X_AUTH_TOKEN_HEADER, accountToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.accounts[0].username", isEmptyOrNullString()))
                .andExpect(jsonPath("$._embedded.accounts[0].email", is(ACCOUNT_EMAIL + TMP_SUFFIX)));
    }

    @Test
    public void projectMembers() throws Exception {
        AccountRegistrationDto owner = getDefaultTestAccount();
        MvcResult ownerMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(owner)))
                .andExpect(status().isOk())
                .andReturn();

        Integer ownerId = getIdFromJson(ownerMvcResult.getResponse().getContentAsString());

        String ownerToken = createToken(owner.getAccountFromDto());
        Project project = getDefaultProject();
        MvcResult projectMvcResult = mockMvc.perform(post("/api/" + ownerId + "/createProject")
                .header(X_AUTH_TOKEN_HEADER, ownerToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)))
                .andReturn();

        Integer projectId = getIdFromJson(projectMvcResult.getResponse().getContentAsString());

        AccountRegistrationDto applicant = getDefaultTestAccount(TMP_SUFFIX);
        MvcResult applicantMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(applicant)))
                .andExpect(status().isOk())
                .andReturn();

        Integer applicantId = getIdFromJson(applicantMvcResult.getResponse().getContentAsString());

        String applicantToken = createToken(applicant.getAccountFromDto());
        mockMvc.perform(post("/api/" + applicantId + "/joinProject/" + projectId)
                .header(X_AUTH_TOKEN_HEADER, applicantToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)));

        mockMvc.perform(get("/api/projects/" + projectId + "/inboxInvitations")
                .header(X_AUTH_TOKEN_HEADER, applicantToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.accounts[0].id", is(applicantId)))
                .andExpect(jsonPath("$._embedded.accounts[0].username", is(ACCOUNT_USERNAME + TMP_SUFFIX)))
                .andExpect(jsonPath("$._embedded.accounts[0].email", is(ACCOUNT_EMAIL + TMP_SUFFIX)));

        mockMvc.perform(post("/api/" + ownerId + "/confirmProjectInboxInvitation/" + projectId + "/" + applicantId)
                .header(X_AUTH_TOKEN_HEADER, ownerToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/projects/" + projectId + "/members")
                .header(X_AUTH_TOKEN_HEADER, ownerToken))
                .andExpect(jsonPath("$._embedded.accounts[0].id", is(applicantId)))
                .andExpect(jsonPath("$._embedded.accounts[0].username", is(ACCOUNT_USERNAME + TMP_SUFFIX)))
                .andExpect(jsonPath("$._embedded.accounts[0].email", is(ACCOUNT_EMAIL + TMP_SUFFIX)));
    }

    @Test
    public void projectRequests() throws Exception {
        AccountRegistrationDto owner = getDefaultTestAccount();
        MvcResult ownerMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(owner)))
                .andExpect(status().isOk())
                .andReturn();

        Integer ownerId = getIdFromJson(ownerMvcResult.getResponse().getContentAsString());

        String ownerToken = createToken(owner.getAccountFromDto());
        Project project = getDefaultProject();
        MvcResult projectMvcResult = mockMvc.perform(post("/api/" + ownerId + "/createProject")
                .header(X_AUTH_TOKEN_HEADER, ownerToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)))
                .andReturn();

        Integer projectId = getIdFromJson(projectMvcResult.getResponse().getContentAsString());

        AccountRegistrationDto applicant = getDefaultTestAccount(TMP_SUFFIX);
        MvcResult applicantMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(applicant)))
                .andExpect(status().isOk())
                .andReturn();

        Integer applicantId = getIdFromJson(applicantMvcResult.getResponse().getContentAsString());

        String applicantToken = createToken(applicant.getAccountFromDto());
        mockMvc.perform(post("/api/" + applicantId + "/joinProject/" + projectId)
                .header(X_AUTH_TOKEN_HEADER, applicantToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)));

        mockMvc.perform(get("/api/accounts/" + applicantId + "/projectRequests")
                .header(X_AUTH_TOKEN_HEADER, applicantToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.projects[0].id", is(projectId)))
                .andExpect(jsonPath("$._embedded.projects[0].name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$._embedded.projects[0].isVisible", is(VISIBLE_PROJECT)));
    }

    @Test
    public void projectInvitations() throws Exception {
        AccountRegistrationDto owner = getDefaultTestAccount();
        MvcResult ownerMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(owner)))
                .andExpect(status().isOk())
                .andReturn();

        Integer ownerId = getIdFromJson(ownerMvcResult.getResponse().getContentAsString());

        String ownerToken = createToken(owner.getAccountFromDto());
        Project project = getDefaultProject();
        MvcResult projectMvcResult = mockMvc.perform(post("/api/" + ownerId + "/createProject")
                .header(X_AUTH_TOKEN_HEADER, ownerToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)))
                .andReturn();

        Integer projectId = getIdFromJson(projectMvcResult.getResponse().getContentAsString());

        AccountRegistrationDto invitee = getDefaultTestAccount(TMP_SUFFIX);
        MvcResult applicantMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(invitee)))
                .andExpect(status().isOk())
                .andReturn();

        Integer inviteeId = getIdFromJson(applicantMvcResult.getResponse().getContentAsString());

        String inviteeToken = createToken(invitee.getAccountFromDto());
        mockMvc.perform(post("/api/" + ownerId + "/inviteProjectEmail/" +
                projectId + "?email=" + (ACCOUNT_EMAIL + TMP_SUFFIX))
                .header(X_AUTH_TOKEN_HEADER, ownerToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(project)))
                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)));

        mockMvc.perform(get("/api/accounts/" + inviteeId + "/projectInvitations")
                .header(X_AUTH_TOKEN_HEADER, inviteeToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.projects[0].id", is(projectId)))
                .andExpect(jsonPath("$._embedded.projects[0].name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$._embedded.projects[0].isVisible", is(VISIBLE_PROJECT)));
    }

    @Test
    public void confirmProjectInvitation() throws Exception {
        AccountRegistrationDto owner = getDefaultTestAccount();
        MvcResult ownerMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(owner)))
                .andExpect(status().isOk())
                .andReturn();

        Integer ownerId = getIdFromJson(ownerMvcResult.getResponse().getContentAsString());

        String ownerToken = createToken(owner.getAccountFromDto());
        Project project = getDefaultProject();
        MvcResult projectMvcResult = mockMvc.perform(post("/api/" + ownerId + "/createProject")
                .header(X_AUTH_TOKEN_HEADER, ownerToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)))
                .andReturn();

        Integer projectId = getIdFromJson(projectMvcResult.getResponse().getContentAsString());

        AccountRegistrationDto invitee = getDefaultTestAccount(TMP_SUFFIX);
        MvcResult applicantMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(invitee)))
                .andExpect(status().isOk())
                .andReturn();

        Integer inviteeId = getIdFromJson(applicantMvcResult.getResponse().getContentAsString());

        String inviteeToken = createToken(invitee.getAccountFromDto());
        mockMvc.perform(post("/api/" + ownerId + "/inviteProjectEmail/" +
                projectId + "?email=" + (ACCOUNT_EMAIL + TMP_SUFFIX))
                .header(X_AUTH_TOKEN_HEADER, ownerToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(project)))
                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)));

        mockMvc.perform(get("/api/accounts/" + inviteeId + "/projectInvitations")
                .header(X_AUTH_TOKEN_HEADER, inviteeToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.projects[0].id", is(projectId)))
                .andExpect(jsonPath("$._embedded.projects[0].name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$._embedded.projects[0].isVisible", is(VISIBLE_PROJECT)));

        mockMvc.perform(post("/api/" + inviteeId + "/confirmProjectInvitation/" + projectId)
                .header(X_AUTH_TOKEN_HEADER, inviteeToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/projects/" + projectId + "/members")
                .header(X_AUTH_TOKEN_HEADER, ownerToken))
                .andExpect(jsonPath("$._embedded.accounts[0].id", is(inviteeId)))
                .andExpect(jsonPath("$._embedded.accounts[0].username", is(ACCOUNT_USERNAME + TMP_SUFFIX)))
                .andExpect(jsonPath("$._embedded.accounts[0].email", is(ACCOUNT_EMAIL + TMP_SUFFIX)));
    }

    @Test
    public void refuseProjectInboxInvitation() throws Exception {
        AccountRegistrationDto owner = getDefaultTestAccount();
        MvcResult ownerMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(owner)))
                .andExpect(status().isOk())
                .andReturn();

        Integer ownerId = getIdFromJson(ownerMvcResult.getResponse().getContentAsString());

        String ownerToken = createToken(owner.getAccountFromDto());
        Project project = getDefaultProject();
        MvcResult projectMvcResult = mockMvc.perform(post("/api/" + ownerId + "/createProject")
                .header(X_AUTH_TOKEN_HEADER, ownerToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)))
                .andReturn();

        Integer projectId = getIdFromJson(projectMvcResult.getResponse().getContentAsString());

        AccountRegistrationDto applicant = getDefaultTestAccount(TMP_SUFFIX);
        MvcResult applicantMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(applicant)))
                .andExpect(status().isOk())
                .andReturn();

        Integer applicantId = getIdFromJson(applicantMvcResult.getResponse().getContentAsString());

        String applicantToken = createToken(applicant.getAccountFromDto());
        mockMvc.perform(post("/api/" + applicantId + "/joinProject/" + projectId)
                .header(X_AUTH_TOKEN_HEADER, applicantToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)));

        mockMvc.perform(get("/api/projects/" + projectId + "/inboxInvitations")
                .header(X_AUTH_TOKEN_HEADER, applicantToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.accounts[0].id", is(applicantId)))
                .andExpect(jsonPath("$._embedded.accounts[0].username", is(ACCOUNT_USERNAME + TMP_SUFFIX)))
                .andExpect(jsonPath("$._embedded.accounts[0].email", is(ACCOUNT_EMAIL + TMP_SUFFIX)));

        mockMvc.perform(post("/api/" + ownerId + "/refuseProjectInboxInvitation/" + projectId + "/" + applicantId)
                .header(X_AUTH_TOKEN_HEADER, ownerToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/projects/" + projectId + "/inboxInvitations")
                .header(X_AUTH_TOKEN_HEADER, ownerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.accounts.length()", is(0)));
    }

    @Test
    public void refuseProjectInvitation() throws Exception {
        AccountRegistrationDto account = getDefaultTestAccount();
        MvcResult accountMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(account)))
                .andExpect(status().isOk())
                .andReturn();

        Integer accountId = getIdFromJson(accountMvcResult.getResponse().getContentAsString());

        String accountToken = createToken(account.getAccountFromDto());
        Project project = getDefaultProject();
        MvcResult projectMvcResult = mockMvc.perform(post("/api/" + accountId + "/createProject")
                .header(X_AUTH_TOKEN_HEADER, accountToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)))
                .andReturn();

        Integer projectId = getIdFromJson(projectMvcResult.getResponse().getContentAsString());

        mockMvc.perform(post("/api/" + accountId + "/inviteProjectEmail/" +
                projectId + "?email=" + (ACCOUNT_EMAIL + TMP_SUFFIX))
                .header(X_AUTH_TOKEN_HEADER, accountToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(project)))
                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)));

        AccountRegistrationDto invitee = getDefaultTestAccount(TMP_SUFFIX);
        MvcResult inviteeMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(invitee)))
                .andExpect(status().isOk())
                .andReturn();

        Integer inviteeId = getIdFromJson(inviteeMvcResult.getResponse().getContentAsString());
        String inviteeToken = createToken(invitee.getAccountFromDto());

        mockMvc.perform(get("/api/accounts/" + inviteeId + "/projectInvitations")
                .header(X_AUTH_TOKEN_HEADER, inviteeToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.projects[0].id", is(projectId)))
                .andExpect(jsonPath("$._embedded.projects[0].name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$._embedded.projects[0].isVisible", is(VISIBLE_PROJECT)));

        mockMvc.perform(post("/api/" + inviteeId + "/refuseProjectInvitation/" + projectId)
                .header(X_AUTH_TOKEN_HEADER, inviteeToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/accounts/" + inviteeId + "/projectInvitations")
                .header(X_AUTH_TOKEN_HEADER, inviteeToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.projects.length()", is(0)));
    }

    @Test
    public void kickFromProject() throws Exception {
        AccountRegistrationDto owner = getDefaultTestAccount();
        MvcResult ownerMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(owner)))
                .andExpect(status().isOk())
                .andReturn();

        Integer ownerId = getIdFromJson(ownerMvcResult.getResponse().getContentAsString());

        String ownerToken = createToken(owner.getAccountFromDto());
        Project project = getDefaultProject();
        MvcResult projectMvcResult = mockMvc.perform(post("/api/" + ownerId + "/createProject")
                .header(X_AUTH_TOKEN_HEADER, ownerToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)))
                .andReturn();

        Integer projectId = getIdFromJson(projectMvcResult.getResponse().getContentAsString());

        AccountRegistrationDto applicant = getDefaultTestAccount(TMP_SUFFIX);
        MvcResult applicantMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(applicant)))
                .andExpect(status().isOk())
                .andReturn();

        Integer applicantId = getIdFromJson(applicantMvcResult.getResponse().getContentAsString());

        String applicantToken = createToken(applicant.getAccountFromDto());
        mockMvc.perform(post("/api/" + applicantId + "/joinProject/" + projectId)
                .header(X_AUTH_TOKEN_HEADER, applicantToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)));

        mockMvc.perform(get("/api/projects/" + projectId + "/inboxInvitations")
                .header(X_AUTH_TOKEN_HEADER, applicantToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.accounts[0].id", is(applicantId)))
                .andExpect(jsonPath("$._embedded.accounts[0].username", is(ACCOUNT_USERNAME + TMP_SUFFIX)))
                .andExpect(jsonPath("$._embedded.accounts[0].email", is(ACCOUNT_EMAIL + TMP_SUFFIX)));

        mockMvc.perform(post("/api/" + ownerId + "/confirmProjectInboxInvitation/" + projectId + "/" + applicantId)
                .header(X_AUTH_TOKEN_HEADER, ownerToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/projects/" + projectId + "/members")
                .header(X_AUTH_TOKEN_HEADER, ownerToken))
                .andExpect(jsonPath("$._embedded.accounts[0].id", is(applicantId)))
                .andExpect(jsonPath("$._embedded.accounts[0].username", is(ACCOUNT_USERNAME + TMP_SUFFIX)))
                .andExpect(jsonPath("$._embedded.accounts[0].email", is(ACCOUNT_EMAIL + TMP_SUFFIX)));

        mockMvc.perform(post("/api/" + ownerId + "/kickFromProject/" + projectId + "/" + applicantId)
                .header(X_AUTH_TOKEN_HEADER, ownerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)));

        mockMvc.perform(get("/api/projects/" + projectId + "/members")
                .header(X_AUTH_TOKEN_HEADER, ownerToken))
                .andExpect(jsonPath("$._embedded.accounts.length()", is(0)));
    }
}
