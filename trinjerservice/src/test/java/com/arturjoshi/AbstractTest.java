package com.arturjoshi;

import com.arturjoshi.account.Account;
import com.arturjoshi.account.AccountCredentials;
import com.arturjoshi.project.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;

public abstract class AbstractTest {

    protected MockMvc mockMvc;

    protected HttpMessageConverter mappingJackson2HttpMessageConverter;

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);
    }

    @Autowired
    protected WebApplicationContext webApplicationContext;

    protected final String X_AUTH_TOKEN_HEADER = "X-Auth-Token";

    protected final String ACCOUNT_PASSWORD = "testpassword";
    protected final String ACCOUNT_USERNAME = "testusername";
    protected final String ACCOUNT_EMAIL = "testemail";

    protected final String PROJECT_NAME = "testprojectname";
    protected final boolean VISIBLE_PROJECT = true;
    protected final boolean NON_VISIBLE_PROJECT = false;
    protected final String INVITEE_EMAIL = "testinviteeemail";

    protected Account getDefaultTestAccount() {
        Account account = new Account();
        AccountCredentials accountCredentials = new AccountCredentials();
        accountCredentials.setPassword(ACCOUNT_PASSWORD);
        account.setCredentials(accountCredentials);
        account.setUsername(ACCOUNT_USERNAME);
        account.setEmail(ACCOUNT_EMAIL);
        return account;
    }

    protected Project getDefaultProject() {
        Project project = new Project();
        project.setName(PROJECT_NAME);
        project.setIsVisible(VISIBLE_PROJECT);
        return project;
    }
}
