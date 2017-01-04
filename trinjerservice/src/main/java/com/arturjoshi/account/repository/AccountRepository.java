package com.arturjoshi.account.repository;

import com.arturjoshi.account.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by arturjoshi on 04-Jan-17.
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    Account findByUsername(String username);
}
