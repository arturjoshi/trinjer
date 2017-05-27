package com.arturjoshi;

import com.arturjoshi.account.Account;
import com.arturjoshi.account.repository.AccountRepository;
import com.arturjoshi.authentication.AccountDetails;
import com.arturjoshi.authentication.StatelessAuthenticationFilter;
import com.arturjoshi.authentication.dto.AccountRegistrationDto;
import com.arturjoshi.authentication.token.TokenHandler;
import com.arturjoshi.project.Project;
import com.arturjoshi.project.dto.ProjectInvitationDto;
import com.arturjoshi.project.entities.ProjectAccountPermission;
import com.arturjoshi.project.entities.ProjectAccountProfile;
import com.arturjoshi.project.repository.ProjectAccountPermissionRepository;
import com.arturjoshi.project.repository.ProjectAccountProfileRepository;
import com.arturjoshi.project.repository.ProjectRepository;
import com.arturjoshi.sprint.Sprint;
import com.arturjoshi.sprint.repository.SprintRepository;
import com.arturjoshi.ticket.issue.Bug;
import com.arturjoshi.ticket.issue.dto.IssueDto;
import com.arturjoshi.ticket.story.Story;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

public abstract class AbstractTest implements TestConst {

    private HttpMessageConverter mappingJackson2HttpMessageConverter;
    private ObjectMapper objectMapper = new ObjectMapper();
    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    private TokenHandler tokenHandler;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private ProjectAccountPermissionRepository projectAccountPermissionRepository;

    @Autowired
    private ProjectAccountProfileRepository projectAccountProfileRepository;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);
    }

    @Before
    public void setup() {
        Collection<StatelessAuthenticationFilter> filterCollection = webApplicationContext.getBeansOfType(StatelessAuthenticationFilter.class).values();
        Filter[] filters = filterCollection.toArray(new Filter[filterCollection.size()]);
        this.mockMvc = webAppContextSetup(webApplicationContext).addFilters(filters).build();
        sprintRepository.deleteAll();
        projectAccountPermissionRepository.deleteAll();
        projectAccountProfileRepository.deleteAll();
        projectRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @After
    public void clean() {
        sprintRepository.deleteAll();
        projectAccountPermissionRepository.deleteAll();
        projectAccountProfileRepository.deleteAll();
        projectRepository.deleteAll();
        accountRepository.deleteAll();
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    protected Integer getIdFromJson(String json) throws IOException {
        Map<String, Integer> map = objectMapper.readValue(json, HashMap.class);
        return map.get("id");
    }

    protected AccountRegistrationDto getDefaultTestAccount() {
        AccountRegistrationDto account = new AccountRegistrationDto();
        account.setUsername(ACCOUNT_USERNAME);
        account.setEmail(ACCOUNT_EMAIL);
        account.setPassword(ACCOUNT_PASSWORD);
        return account;
    }

    protected AccountRegistrationDto getDefaultTestAccount(String prefix) {
        AccountRegistrationDto account = new AccountRegistrationDto();
        account.setPassword(ACCOUNT_PASSWORD + prefix);
        account.setUsername(ACCOUNT_USERNAME + prefix);
        account.setEmail(ACCOUNT_EMAIL + prefix);
        return account;
    }

    protected Project getDefaultProject() {
        Project project = new Project();
        project.setName(PROJECT_NAME);
        project.setIsVisible(VISIBLE_PROJECT);
        return project;
    }

    protected Sprint getDefaultSprint() {
        Sprint sprint = new Sprint();
        sprint.setDescription(SPRINT_DESCRIPTION);
        sprint.setStartDate(LocalDate.now());
        sprint.setEndDate(LocalDate.now().plusWeeks(1));
        return sprint;
    }

    protected Story getDefaultStory() {
        Story story = new Story();
        story.setDescription(STORY_DESCRIPTION);
        story.setSummary(STORY_SUMMARY);
        story.setAcceptanceCriteria(STORY_ACCEPTANCE_CRITERIA);
        story.setPriority(STORY_PRIORITY);
        story.setStatus(STORY_STATUS);
        story.setEstimate(STORY_ESTIMATE);
        return story;
    }

    protected IssueDto getDefaultBug() {
        IssueDto bug = getDefaultIssue();
        bug.setStepsToReproduce(BUG_STEPS_TO_REPRODUCE);
        return bug;
    }

    protected IssueDto getDefaultIssue() {
        IssueDto bug = new IssueDto();
        bug.setSummary(ISSUE_SUMMARY);
        bug.setDescription(ISSUE_DESCRIPTION);
        bug.setPriority(ISSUE_PRIORITY);
        bug.setStatus(ISSUE_STATUS);
        bug.setResolution(ISSUE_RESOLUTION);
        return bug;
    }

    protected ProjectInvitationDto getInvitationDto(String email,
                                                    ProjectAccountPermission.ProjectPermission projectPermission,
                                                    ProjectAccountProfile.ProjectProfile projectProfile) {
        ProjectInvitationDto projectInvitationDto = new ProjectInvitationDto();
        projectInvitationDto.setEmail(email);
        projectInvitationDto.setPermission(Optional.ofNullable(projectPermission)
                .orElse(ProjectAccountPermission.ProjectPermission.MEMBER));
        projectInvitationDto.setProfile(Optional.ofNullable(projectProfile)
                .orElse(ProjectAccountProfile.ProjectProfile.DEVELOPER));
        return projectInvitationDto;
    }

    protected String createToken(Account account) {
        return tokenHandler.createTokenForUser(new AccountDetails(account));
    }
}
