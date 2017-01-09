package com.arturjoshi.account.repository;

import com.arturjoshi.account.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Created by arturjoshi on 04-Jan-17.
 */
@RepositoryRestResource(path = "accounts")
public interface AccountRepository extends CrudRepository<Account, Long> {

    @Override
    @RestResource(exported = false)
    <S extends Account> S save(S s);

    @Override
    @RestResource(exported = false)
    <S extends Account> Iterable<S> save(Iterable<S> iterable);

    @Override
    @RestResource(exported = false)
    void delete(Long aLong);

    @Override
    @RestResource(exported = false)
    void delete(Account account);

    @Override
    @RestResource(exported = false)
    void delete(Iterable<? extends Account> iterable);

    @Override
    @RestResource(exported = false)
    void deleteAll();

    Account findByUsername(String username);

    Account findByEmail(String email);
}
