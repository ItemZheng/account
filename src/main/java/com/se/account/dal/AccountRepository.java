package com.se.account.dal;

import com.se.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account getAccountByIdAndRemoved(long accountId, boolean removed);
    Account getAccountBySecuritiesAccountIdAndRemoved(long securitiesAccountId, boolean removed);
}
