package com.arturjoshi.account;

import com.arturjoshi.AbstractTest;
import com.arturjoshi.account.repository.AccountRepository;
import com.arturjoshi.authentication.controllers.AuthenticationController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.*;
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
public class AccountTest extends AbstractTest {

    private final String ACCOUNT_PASSWORD = "testpassword";
    private final String ACCOUNT_USERNAME = "testusername";
    private final String ACCOUNT_EMAIL = "testemail";

    @Autowired
    private AccountRepository accountRepository;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        accountRepository.deleteAll();
    }

    @Test
    public void registerNewAccountTest() throws Exception {
        Account account = getDefaultTestAccount();

        mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(account)))
                .andExpect(status().isOk());

        Long foundedId = accountRepository.findByUsername(ACCOUNT_USERNAME).getId();

        mockMvc.perform(get("/api/accounts/" + foundedId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(foundedId.intValue())))
                .andExpect(jsonPath("$.username", is(ACCOUNT_USERNAME)))
                .andExpect(jsonPath("$.email", is(ACCOUNT_EMAIL)));
    }

    @Test
    public void registerWithExistingEmailTest() throws Exception {
        Account account = getDefaultTestAccount();

        mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(account)))
                .andExpect(status().isOk());

        account.setUsername(ACCOUNT_USERNAME + "tmp");
        mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(account)))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$",
                        is(AuthenticationController.AuthenticationControllerConstants.ACCOUNT_EMAIL_EXISTS)));

    }

    @Test
    public void registerWithExistingUsernameTest() throws Exception {
        Account account = getDefaultTestAccount();

        mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(account)))
                .andExpect(status().isOk());

        account.setEmail(ACCOUNT_EMAIL + "tmp");
        mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(account)))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$",
                        is(AuthenticationController.AuthenticationControllerConstants.ACCOUNT_USERNAME_EXISTS)));

    }

    @Test
    public void authenticateTest() throws Exception {
        Account account = getDefaultTestAccount();
        mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(account)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/authenticate/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(account)))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$",
                        not(AuthenticationController.AuthenticationControllerConstants.BAD_CREDENTIALS)))
                .andExpect(jsonPath("$",
                        not(AuthenticationController.AuthenticationControllerConstants.NO_SUCH_ACCOUNT)))
                .andExpect(status().isOk());
    }

    @Test
    public void authenticateBadCredentialsTest() throws Exception {
        Account account = getDefaultTestAccount();
        accountRepository.save(account);

        Account wrongAccount = getDefaultTestAccount();
        AccountCredentials wrongCredentials = new AccountCredentials();
        wrongCredentials.setPassword("wrongcredentials");
        wrongAccount.setCredentials(wrongCredentials);
        mockMvc.perform(post("/api/authenticate/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(wrongAccount)))
                .andExpect(jsonPath("$",
                        is(AuthenticationController.AuthenticationControllerConstants.BAD_CREDENTIALS)))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void authenticateNonExistingAccountCredentialsTest() throws Exception {
        Account account = getDefaultTestAccount();
        mockMvc.perform(post("/api/register/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(account)))
                .andExpect(status().isOk());

        account.setUsername(ACCOUNT_USERNAME + "tmp");
        mockMvc.perform(post("/api/authenticate/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.json(account)))
                .andExpect(jsonPath("$",
                        is(AuthenticationController.AuthenticationControllerConstants.NO_SUCH_ACCOUNT)))
                .andExpect(status().is5xxServerError());
    }

    private Account getDefaultTestAccount() {
        Account account = new Account();
        AccountCredentials accountCredentials = new AccountCredentials();
        accountCredentials.setPassword(ACCOUNT_PASSWORD);
        account.setCredentials(accountCredentials);
        account.setUsername(ACCOUNT_USERNAME);
        account.setEmail(ACCOUNT_EMAIL);
        return account;
    }
}
