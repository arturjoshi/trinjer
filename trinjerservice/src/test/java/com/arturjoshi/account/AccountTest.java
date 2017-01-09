package com.arturjoshi.account;

import com.arturjoshi.account.repository.AccountRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by arturjoshi on 08-Jan-17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountTest {

    private final String ACCOUNT_PASSWORD = "testpassword";
    private final String ACCOUNT_USERNAME = "testusername";
    private final String ACCOUNT_EMAIL = "testemail";
    private final ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder(256);

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void createAndGetAccountTest() {
        Account account = new Account();
        final String EXPECTED_CREATED_TIME = account.getCreatedTime();
        AccountCredentials credentials = new AccountCredentials();
        credentials.setPassword(ACCOUNT_PASSWORD);
        account.setCredentials(credentials);
        account.setUsername(ACCOUNT_USERNAME);
        account.setEmail(ACCOUNT_EMAIL);
        accountRepository.save(account);

        Account founded = accountRepository.findOne(account.getId());
        Assert.assertEquals(ACCOUNT_EMAIL, founded.getEmail());
        Assert.assertEquals(ACCOUNT_USERNAME, founded.getUsername());
        Assert.assertEquals(EXPECTED_CREATED_TIME, founded.getCreatedTime());
        Assert.assertEquals(
                passwordEncoder.encodePassword(ACCOUNT_PASSWORD, null),
                passwordEncoder.encodePassword(account.getCredentials().getPassword(), null));

        accountRepository.delete(account);
        Assert.assertNull(accountRepository.findOne(account.getId()));
    }
}
