package com.arturjoshi;

import com.arturjoshi.account.Account;
import com.arturjoshi.account.AccountCredentials;
import com.arturjoshi.authentication.AccountDetails;
import com.arturjoshi.authentication.token.TokenHandler;
import com.arturjoshi.project.Project;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTest implements TestConst {

    private HttpMessageConverter mappingJackson2HttpMessageConverter;
    private ObjectMapper objectMapper = new ObjectMapper();
    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    private TokenHandler tokenHandler;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);
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

    protected Account getDefaultTestAccount() {
        Account account = new Account();
        AccountCredentials accountCredentials = new AccountCredentials();
        accountCredentials.setPassword(ACCOUNT_PASSWORD);
        account.setCredentials(accountCredentials);
        account.setUsername(ACCOUNT_USERNAME);
        account.setEmail(ACCOUNT_EMAIL);
        return account;
    }

    protected Account getDefaultTestAccount(String prefix) {
        Account account = new Account();
        AccountCredentials accountCredentials = new AccountCredentials();
        accountCredentials.setPassword(ACCOUNT_PASSWORD + prefix);
        account.setCredentials(accountCredentials);
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

    protected String createToken(Account account) {
        return tokenHandler.createTokenForUser(new AccountDetails(account));
    }
}
