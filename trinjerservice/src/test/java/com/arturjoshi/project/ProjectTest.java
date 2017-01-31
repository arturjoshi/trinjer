package com.arturjoshi.project;

import com.arturjoshi.AbstractTest;
import com.arturjoshi.account.Account;
import com.arturjoshi.account.repository.AccountRepository;
import com.arturjoshi.milestones.repository.MilestoneRepository;
import com.arturjoshi.project.repository.ProjectAccountPermissionRepository;
import com.arturjoshi.project.repository.ProjectAccountProfileRepository;
import com.arturjoshi.project.repository.ProjectRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by arturjoshi on 08-Jan-17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectTest extends AbstractTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MilestoneRepository milestoneRepository;

    @Autowired
    private ProjectAccountPermissionRepository projectAccountPermissionRepository;

    @Autowired
    private ProjectAccountProfileRepository projectAccountProfileRepository;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        milestoneRepository.deleteAll();
        projectAccountPermissionRepository.deleteAll();
        projectAccountProfileRepository.deleteAll();
        projectRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @After
    public void clean() {
        projectAccountPermissionRepository.deleteAll();
        projectAccountProfileRepository.deleteAll();
        for (Project project : projectRepository.findAll()) {
            project.setProjectOwner(null);
            projectRepository.save(project);
        }
        projectRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    public void createProjectTest() throws Exception {
        Account account = getDefaultTestAccount();
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
    public void inviteToProject() throws Exception {
        Account owner = getDefaultTestAccount();
        MvcResult accountMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(owner)))
                .andExpect(status().isOk())
                .andReturn();

        Integer ownerId = getIdFromJson(accountMvcResult.getResponse().getContentAsString());

        String ownerToken = createToken(owner);
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
                projectId + "?email=" + (ACCOUNT_EMAIL + TMP_ACCOUNT_PREFIX))
                .header(X_AUTH_TOKEN_HEADER, ownerToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(project)))
                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)));

        Account invitee = getDefaultTestAccount(TMP_ACCOUNT_PREFIX);
        MvcResult applicantMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(invitee)))
                .andExpect(status().isOk())
                .andReturn();

        Integer inviteeId = getIdFromJson(applicantMvcResult.getResponse().getContentAsString());
        String inviteeToken = createToken(invitee);
        mockMvc.perform(get("/api/accounts/" + inviteeId + "/projectInvitations")
                .header(X_AUTH_TOKEN_HEADER, inviteeToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.projects[0].id", is(projectId)))
                .andExpect(jsonPath("$._embedded.projects[0].name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$._embedded.projects[0].isVisible", is(VISIBLE_PROJECT)));
    }

    @Test
    public void joinProject() throws Exception {
        Account owner = getDefaultTestAccount();
        MvcResult ownerMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(owner)))
                .andExpect(status().isOk())
                .andReturn();

        Integer ownerId = getIdFromJson(ownerMvcResult.getResponse().getContentAsString());

        String ownerToken = createToken(owner);
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

        Account applicant = getDefaultTestAccount(TMP_ACCOUNT_PREFIX);
        MvcResult applicantMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(applicant)))
                .andExpect(status().isOk())
                .andReturn();

        Integer applicantId = getIdFromJson(applicantMvcResult.getResponse().getContentAsString());

        String applicantToken = createToken(applicant);
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
                .andExpect(jsonPath("$._embedded.accounts[0].username", is(ACCOUNT_USERNAME + TMP_ACCOUNT_PREFIX)))
                .andExpect(jsonPath("$._embedded.accounts[0].email", is(ACCOUNT_EMAIL + TMP_ACCOUNT_PREFIX)));
    }

    @Test
    public void ownedProject() throws Exception {
        Account account = getDefaultTestAccount();
        MvcResult accountMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(account)))
                .andExpect(status().isOk())
                .andReturn();

        Integer accountId = getIdFromJson(accountMvcResult.getResponse().getContentAsString());

        String accountToken = createToken(account);
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
        Account account = getDefaultTestAccount();
        MvcResult accountMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(account)))
                .andExpect(status().isOk())
                .andReturn();

        Integer accountId = getIdFromJson(accountMvcResult.getResponse().getContentAsString());

        String accountToken = createToken(account);
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
                projectId + "?email=" + ACCOUNT_EMAIL + TMP_ACCOUNT_PREFIX)
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
                .andExpect(jsonPath("$._embedded.accounts[0].email", is(ACCOUNT_EMAIL + TMP_ACCOUNT_PREFIX)));
    }

    @Test
    public void projectMembers() throws Exception {
        Account owner = getDefaultTestAccount();
        MvcResult ownerMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(owner)))
                .andExpect(status().isOk())
                .andReturn();

        Integer ownerId = getIdFromJson(ownerMvcResult.getResponse().getContentAsString());

        String ownerToken = createToken(owner);
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

        Account applicant = getDefaultTestAccount(TMP_ACCOUNT_PREFIX);
        MvcResult applicantMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(applicant)))
                .andExpect(status().isOk())
                .andReturn();

        Integer applicantId = getIdFromJson(applicantMvcResult.getResponse().getContentAsString());

        String applicantToken = createToken(applicant);
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
                .andExpect(jsonPath("$._embedded.accounts[0].username", is(ACCOUNT_USERNAME + TMP_ACCOUNT_PREFIX)))
                .andExpect(jsonPath("$._embedded.accounts[0].email", is(ACCOUNT_EMAIL + TMP_ACCOUNT_PREFIX)));

        mockMvc.perform(post("/api/" + ownerId + "/confirmProjectInboxInvitation/" + projectId + "/" + applicantId)
                .header(X_AUTH_TOKEN_HEADER, ownerToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/projects/" + projectId + "/members")
                .header(X_AUTH_TOKEN_HEADER, ownerToken))
                .andExpect(jsonPath("$._embedded.accounts[0].id", is(applicantId)))
                .andExpect(jsonPath("$._embedded.accounts[0].username", is(ACCOUNT_USERNAME + TMP_ACCOUNT_PREFIX)))
                .andExpect(jsonPath("$._embedded.accounts[0].email", is(ACCOUNT_EMAIL + TMP_ACCOUNT_PREFIX)));
    }

    @Test
    public void projectRequests() throws Exception {
        Account owner = getDefaultTestAccount();
        MvcResult ownerMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(owner)))
                .andExpect(status().isOk())
                .andReturn();

        Integer ownerId = getIdFromJson(ownerMvcResult.getResponse().getContentAsString());

        String ownerToken = createToken(owner);
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

        Account applicant = getDefaultTestAccount(TMP_ACCOUNT_PREFIX);
        MvcResult applicantMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(applicant)))
                .andExpect(status().isOk())
                .andReturn();

        Integer applicantId = getIdFromJson(applicantMvcResult.getResponse().getContentAsString());

        String applicantToken = createToken(applicant);
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
        Account owner = getDefaultTestAccount();
        MvcResult ownerMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(owner)))
                .andExpect(status().isOk())
                .andReturn();

        Integer ownerId = getIdFromJson(ownerMvcResult.getResponse().getContentAsString());

        String ownerToken = createToken(owner);
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

        Account invitee = getDefaultTestAccount(TMP_ACCOUNT_PREFIX);
        MvcResult applicantMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(invitee)))
                .andExpect(status().isOk())
                .andReturn();

        Integer inviteeId = getIdFromJson(applicantMvcResult.getResponse().getContentAsString());

        String inviteeToken = createToken(invitee);
        mockMvc.perform(post("/api/" + ownerId + "/inviteProjectEmail/" +
                projectId + "?email=" + (ACCOUNT_EMAIL + TMP_ACCOUNT_PREFIX))
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
        Account owner = getDefaultTestAccount();
        MvcResult ownerMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(owner)))
                .andExpect(status().isOk())
                .andReturn();

        Integer ownerId = getIdFromJson(ownerMvcResult.getResponse().getContentAsString());

        String ownerToken = createToken(owner);
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

        Account invitee = getDefaultTestAccount(TMP_ACCOUNT_PREFIX);
        MvcResult applicantMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(invitee)))
                .andExpect(status().isOk())
                .andReturn();

        Integer inviteeId = getIdFromJson(applicantMvcResult.getResponse().getContentAsString());

        String inviteeToken = createToken(invitee);
        mockMvc.perform(post("/api/" + ownerId + "/inviteProjectEmail/" +
                projectId + "?email=" + (ACCOUNT_EMAIL + TMP_ACCOUNT_PREFIX))
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
                .andExpect(jsonPath("$._embedded.accounts[0].username", is(ACCOUNT_USERNAME + TMP_ACCOUNT_PREFIX)))
                .andExpect(jsonPath("$._embedded.accounts[0].email", is(ACCOUNT_EMAIL + TMP_ACCOUNT_PREFIX)));
    }

    @Test
    public void refuseProjectInboxInvitation() throws Exception {
        Account owner = getDefaultTestAccount();
        MvcResult ownerMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(owner)))
                .andExpect(status().isOk())
                .andReturn();

        Integer ownerId = getIdFromJson(ownerMvcResult.getResponse().getContentAsString());

        String ownerToken = createToken(owner);
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

        Account applicant = getDefaultTestAccount(TMP_ACCOUNT_PREFIX);
        MvcResult applicantMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(applicant)))
                .andExpect(status().isOk())
                .andReturn();

        Integer applicantId = getIdFromJson(applicantMvcResult.getResponse().getContentAsString());

        String applicantToken = createToken(applicant);
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
                .andExpect(jsonPath("$._embedded.accounts[0].username", is(ACCOUNT_USERNAME + TMP_ACCOUNT_PREFIX)))
                .andExpect(jsonPath("$._embedded.accounts[0].email", is(ACCOUNT_EMAIL + TMP_ACCOUNT_PREFIX)));

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
        Account account = getDefaultTestAccount();
        MvcResult accountMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(account)))
                .andExpect(status().isOk())
                .andReturn();

        Integer accountId = getIdFromJson(accountMvcResult.getResponse().getContentAsString());

        String accountToken = createToken(account);
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
                projectId + "?email=" + (ACCOUNT_EMAIL + TMP_ACCOUNT_PREFIX))
                .header(X_AUTH_TOKEN_HEADER, accountToken)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(project)))
                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)));

        Account invitee = getDefaultTestAccount(TMP_ACCOUNT_PREFIX);
        MvcResult inviteeMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(invitee)))
                .andExpect(status().isOk())
                .andReturn();

        Integer inviteeId = getIdFromJson(inviteeMvcResult.getResponse().getContentAsString());
        String inviteeToken = createToken(invitee);

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
        Account owner = getDefaultTestAccount();
        MvcResult ownerMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(owner)))
                .andExpect(status().isOk())
                .andReturn();

        Integer ownerId = getIdFromJson(ownerMvcResult.getResponse().getContentAsString());

        String ownerToken = createToken(owner);
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

        Account applicant = getDefaultTestAccount(TMP_ACCOUNT_PREFIX);
        MvcResult applicantMvcResult = mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(applicant)))
                .andExpect(status().isOk())
                .andReturn();

        Integer applicantId = getIdFromJson(applicantMvcResult.getResponse().getContentAsString());

        String applicantToken = createToken(applicant);
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
                .andExpect(jsonPath("$._embedded.accounts[0].username", is(ACCOUNT_USERNAME + TMP_ACCOUNT_PREFIX)))
                .andExpect(jsonPath("$._embedded.accounts[0].email", is(ACCOUNT_EMAIL + TMP_ACCOUNT_PREFIX)));

        mockMvc.perform(post("/api/" + ownerId + "/confirmProjectInboxInvitation/" + projectId + "/" + applicantId)
                .header(X_AUTH_TOKEN_HEADER, ownerToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/projects/" + projectId + "/members")
                .header(X_AUTH_TOKEN_HEADER, ownerToken))
                .andExpect(jsonPath("$._embedded.accounts[0].id", is(applicantId)))
                .andExpect(jsonPath("$._embedded.accounts[0].username", is(ACCOUNT_USERNAME + TMP_ACCOUNT_PREFIX)))
                .andExpect(jsonPath("$._embedded.accounts[0].email", is(ACCOUNT_EMAIL + TMP_ACCOUNT_PREFIX)));

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
