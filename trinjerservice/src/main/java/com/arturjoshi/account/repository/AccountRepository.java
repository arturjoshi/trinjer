package com.arturjoshi.account.repository;

import com.arturjoshi.account.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by arturjoshi on 04-Jan-17.
 */
@RepositoryRestResource(path = "accounts")
public interface AccountRepository extends CrudRepository<Account, Long> {

    Account findByUsername(String username);

    Account findByEmail(String email);
}
