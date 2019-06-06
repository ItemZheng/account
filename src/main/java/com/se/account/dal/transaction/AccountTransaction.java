package com.se.account.dal.transaction;

import com.se.account.dal.AccountRepository;
import com.se.account.dal.BalanceRepository;
import com.se.account.dal.RecordRepository;
import com.se.account.domain.Account;
import com.se.account.domain.Balance;
import com.se.account.domain.Record;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

@Component
public class AccountTransaction {
    @Resource
    AccountRepository accountDb;

    @Resource
    BalanceRepository balanceDb;

    @Resource
    RecordRepository recordDb;

    @Transactional
    public void createAccountAndBalanceDB(Account account, Balance balance){
        accountDb.saveAndFlush(account);
        balance.setFundAccountId(account.getId());
        balanceDb.saveAndFlush(balance);
    }

    @Transactional
    public void saveBalanceAndRecordDB(Balance balance, Record record){
        balanceDb.saveAndFlush(balance);
        recordDb.saveAndFlush(record);
    }

    @Transactional
    public void saveAccountAndBalanceDB(Account account, Balance balance){
        accountDb.saveAndFlush(account);
        balanceDb.saveAndFlush(balance);
    }

    @Transactional
    public void reissueDb(Account oldAccount, Balance balance, Account newAccount){
        accountDb.saveAndFlush(oldAccount);
        accountDb.saveAndFlush(newAccount);
        balance.setFundAccountId(newAccount.getId());
        balanceDb.saveAndFlush(balance);
    }

    @Transactional
    public void removeDb(Account account, Balance balance, List<Record> records){
        accountDb.saveAndFlush(account);
        balanceDb.saveAndFlush(balance);
        recordDb.saveAll(records);
    }
}
