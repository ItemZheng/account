package com.se.account.dal;

import com.se.account.domain.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Balance getBalanceByFundAccountIdAndRemoved(long fundAccountId, boolean removed);
}
