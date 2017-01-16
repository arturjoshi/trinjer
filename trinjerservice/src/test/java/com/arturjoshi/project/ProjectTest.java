package com.arturjoshi.project;

import com.arturjoshi.AbstractTest;
import com.arturjoshi.account.Account;
import com.arturjoshi.account.repository.AccountRepository;
import com.arturjoshi.authentication.AccountDetails;
import com.arturjoshi.authentication.token.TokenHandler;
import com.arturjoshi.project.repository.ProjectAccountPermissionRepository;
import com.arturjoshi.project.repository.ProjectAccountProfileRepository;
import com.arturjoshi.project.repository.ProjectRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.is;
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
    private ProjectAccountPermissionRepository projectAccountPermissionRepository;

    @Autowired
    private ProjectAccountProfileRepository projectAccountProfileRepository;

    @Autowired
    private TokenHandler tokenHandler;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
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
        mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(account)))
                .andExpect(status().isOk());

        Long foundedId = accountRepository.findByUsername(ACCOUNT_USERNAME).getId();

        Project project = getDefaultProject();
        mockMvc.perform(post("/api/" + foundedId + "/createProject")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)));
    }

//    @Test
//    public void inviteToProject() throws Exception {
//        Account account = getDefaultTestAccount();
//        mockMvc.perform(post("/api/register/")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(this.json(account)))
//                .andExpect(status().isOk());
//
//        Long foundedId = accountRepository.findByUsername(ACCOUNT_USERNAME).getId();
//
//        String token = tokenHandler.createTokenForUser(new AccountDetails(account));
//        Project project = getDefaultProject();
//        mockMvc.perform(post("/api/" + foundedId + "/createProject")
//                .header(X_AUTH_TOKEN_HEADER, token)
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(this.json(project)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
//                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
//                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
//                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)));
//
//
//        mockMvc.perform(post("/api/" + foundedId + "/inviteProjectEmail/" +
//                project.getId() + "?email=" + INVITEE_EMAIL)
//                .header(X_AUTH_TOKEN_HEADER, token)
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(this.json(project)))
//                .andExpect(jsonPath("$.name", is(PROJECT_NAME)))
//                .andExpect(jsonPath("$.isVisible", is(VISIBLE_PROJECT)))
//                .andExpect(jsonPath("$.projectOwner.username", is(ACCOUNT_USERNAME)))
//                .andExpect(jsonPath("$.projectOwner.email", is(ACCOUNT_EMAIL)));
//
//        List<Project> projectInvitations = new ArrayList<>(
//                accountRepository.findByEmail(INVITEE_EMAIL).getProjectInvitations());
//        Assert.assertNotEquals(0, projectInvitations.size());
//        Project projectInvite = projectInvitations.get(0);
//        Assert.assertEquals(PROJECT_NAME, projectInvite.getName());
//        Assert.assertEquals(VISIBLE_PROJECT, projectInvite.getIsVisible());
//        Assert.assertEquals(ACCOUNT_USERNAME, projectInvite.getProjectOwner().getUsername());
//        Assert.assertEquals(ACCOUNT_EMAIL, projectInvite.getProjectOwner().getEmail());
//
//    }
}
