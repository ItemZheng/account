package com.se.account.service;

import com.se.account.dal.AccountRepository;
import com.se.account.dal.BalanceRepository;
import com.se.account.dal.RecordRepository;
import com.se.account.dal.transaction.AccountTransaction;
import com.se.account.domain.Account;
import com.se.account.domain.Balance;
import com.se.account.domain.Record;
import com.se.account.util.Check;
import com.se.account.util.Constant;
import com.se.account.util.ErrorEnum;
import com.se.account.util.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class AccountService {
    @Resource
    AccountTransaction accountTransaction;

    @Resource
    AccountRepository accountDb;

    @Resource
    BalanceRepository balanceDb;

    @Resource
    RecordRepository recordDb;

    public long create(long securitiesAccountId, String transactionPwd, String withdrawalPwd, int currency, long adminId) throws ServiceException {
        if (!Check.checkValidPassword(transactionPwd) || !Check.checkValidPassword(withdrawalPwd)) {
            log.error("Invalid password: " + transactionPwd + ":" + withdrawalPwd);
            throw new ServiceException(ErrorEnum.ERROR_PASSWORD_INVALID);
        }

        // create new account
        Account account = new Account();
        account.setSecuritiesAccountId(securitiesAccountId);
        account.setTransaction_pwd(transactionPwd);
        account.setWithdrawal_pwd(withdrawalPwd);
        account.setStatus(Constant.ACCOUNT_STATUS_NORMAL);
        Date date = new Date();
        account.setCreate_time(date);
        account.setCreate_staff(adminId);
        account.setModify_time(date);
        account.setModify_staff(adminId);
        account.setRemoved(false);

        // create new balance
        Balance balance = new Balance();
        balance.setCurrency(currency);
        balance.setBalance(0.0);
        balance.setAvailable_balance(0.0);
        balance.setCreate_staff(adminId);
        balance.setCreate_time(date);
        balance.setModify_staff(adminId);
        balance.setModify_time(date);
        balance.setStatus(Constant.BALANCE_STATUS_NORMAL);
        balance.setRemoved(false);

        // todo 证券账户解冻

        // save account and balance
        try {
            accountTransaction.createAccountAndBalanceDB(account, balance);
        } catch (Exception e) {
            log.error("ERROR save account " + e.getMessage());
            throw new ServiceException(ErrorEnum.ERROR_UNKNOWN);
        }
        return account.getId();
    }

    public void validateAccount(Account account) throws ServiceException {
        // check if account id exist
        if (account == null) {
            throw new ServiceException(ErrorEnum.ERROR_ACCOUNT_NOT_EXIST);
        }
        // check account status
        if (account.getStatus() != Constant.ACCOUNT_STATUS_NORMAL) {
            throw new ServiceException(ErrorEnum.ERROR_ACCOUNT_STATUS_ERROR);
        }
    }

    private void validateAccountAndBalance(Account account, Balance balance) throws ServiceException {
        validateAccount(account);
        // check balance
        if (balance == null) {
            throw new ServiceException(ErrorEnum.ERROR_BALANCE_NOT_EXIST);
        }
        // check balance status
        if (balance.getStatus() != Constant.BALANCE_STATUS_NORMAL) {
            throw new ServiceException(ErrorEnum.ERROR_BALANCE_STATUS_ERROR);
        }
    }

    private void checkBalance(Balance balance, double amount) throws ServiceException{
        if (balance.getAvailable_balance() < amount) {
            throw new ServiceException(ErrorEnum.ERROR_BALANCE_NOT_ENOUGH);
        }
    }

    public void operate(long accountId, String withdrawalPwd, double amount, long staffId, int opCode) throws ServiceException {
        if (amount <= 0) {
            throw new ServiceException(ErrorEnum.ERROR_AMOUNT_ERROR);
        }
        // get balance and account
        Account account = accountDb.getAccountByIdAndRemoved(accountId, false);
        Balance balance = balanceDb.getBalanceByFundAccountIdAndRemoved(accountId, false);
        // 验证账户状态
        validateAccountAndBalance(account, balance);
        // check password
        if (!account.getWithdrawal_pwd().equals(withdrawalPwd)) {
            throw new ServiceException(ErrorEnum.ERROR_WITHDRAWAL_PASSWORD_ERROR);
        }
        // check amount
        if (opCode == Constant.BALANCE_OPCODE_WITHDRAWAL) {
            checkBalance(balance, amount);
        }

        // operate money
        Date timeNow = new Date();
        switch (opCode) {
            case Constant.BALANCE_OPCODE_SAVE:
                balance.setAvailable_balance(balance.getAvailable_balance() + amount);
                balance.setBalance(balance.getBalance() + amount);
                break;
            case Constant.BALANCE_OPCODE_WITHDRAWAL:
                balance.setAvailable_balance(balance.getAvailable_balance() - amount);
                balance.setBalance(balance.getBalance() - amount);
                break;
            default:
                throw new ServiceException(ErrorEnum.ERROR_UNKNOWN_BALANCE_OPERATE_TYPE);
        }
        balance.setModify_time(timeNow);
        balance.setModify_staff(staffId);

        // generate record
        Record record = new Record();
        record.setBalanceId(balance.getId());
        record.setAmount(amount);
        record.setOperate_code(opCode);
        record.setPreDecreaseId(0);
        record.setCreate_staff(staffId);
        record.setCreate_time(timeNow);
        record.setModify_staff(staffId);
        record.setModify_time(timeNow);
        // operate balance and record
        try {
            accountTransaction.saveBalanceAndRecordDB(balance, record);
        } catch (Exception e) {
            log.error("ERROR OPERATE BALANCE. OPCODE: " + opCode + " ERROR:" + e.getMessage());
            throw new ServiceException(ErrorEnum.ERROR_UNKNOWN);
        }
    }

    public void reportLoss(long securitiesAccountId, long adminId) throws ServiceException {
        // get account by securitiesAccountId
        Account account = accountDb.getAccountBySecuritiesAccountIdAndRemoved(securitiesAccountId, false);
        if(account == null){
            throw new ServiceException(ErrorEnum.ERROR_SECURITIES_ACCOUNT_NOT_HAS_FUND_ACCOUNT);
        }
        Balance balance = balanceDb.getBalanceByFundAccountIdAndRemoved(account.getId(), false);
        if(balance == null){
            throw new ServiceException(ErrorEnum.ERROR_BALANCE_NOT_EXIST);
        }

        // check status
        if(account.getStatus() == Constant.ACCOUNT_STATUS_REPORT_LOSS){
            throw new ServiceException(ErrorEnum.ERROR_ACCOUNT_ALREADY_REPORT_LOSS);
        }
        if(balance.getStatus() != Constant.BALANCE_STATUS_NORMAL){
            throw new ServiceException(ErrorEnum.ERROR_BALANCE_STATUS_ERROR);
        }

        // modify status
        Date timeNow = new Date();
        account.setStatus(Constant.ACCOUNT_STATUS_REPORT_LOSS);
        account.setModify_time(timeNow);
        account.setModify_staff(adminId);

        balance.setStatus(Constant.BALANCE_STATUS_FREEZE);
        balance.setModify_time(timeNow);
        balance.setModify_staff(adminId);

        // todo 证券帐户下所有的证券予以冻结;
        try {
            accountTransaction.saveAccountAndBalanceDB(account, balance);
        } catch (Exception e) {
            log.error("ERROR REPORT LOSS. ERROR:" + e.getMessage());
            throw new ServiceException(ErrorEnum.ERROR_UNKNOWN);
        }
    }

    public void reissue(long securitiesAccountId, String transactionPwd, String withdrawalPwd, long adminId)throws ServiceException{
        // get account and check status
        Account account = accountDb.getAccountBySecuritiesAccountIdAndRemoved(securitiesAccountId, false);
        if(account == null){
            throw new ServiceException(ErrorEnum.ERROR_SECURITIES_ACCOUNT_NOT_HAS_FUND_ACCOUNT);
        }
        if(account.getStatus() != Constant.ACCOUNT_STATUS_REPORT_LOSS){
            throw new ServiceException(ErrorEnum.ERROR_ACCOUNT_NOT_REPORT_LOSS);
        }

        // get balance and check status
        Balance balance = balanceDb.getBalanceByFundAccountIdAndRemoved(account.getId(), false);
        if(balance == null){
            throw new ServiceException(ErrorEnum.ERROR_BALANCE_NOT_EXIST);
        }
        if(balance.getStatus() != Constant.BALANCE_STATUS_FREEZE){
            throw new ServiceException(ErrorEnum.ERROR_BALANCE_STATUS_ERROR);
        }

        // 重新开设账户
        if(!Check.checkValidPassword(transactionPwd) || !Check.checkValidPassword(withdrawalPwd)){
            log.error("Invalid password: " + transactionPwd + ":" + withdrawalPwd);
            throw new ServiceException(ErrorEnum.ERROR_PASSWORD_INVALID);
        }

        Date timeNow = new Date();
        // 移除旧账户
        account.setTransaction_pwd(transactionPwd);
        account.setWithdrawal_pwd(withdrawalPwd);
        account.setStatus(Constant.ACCOUNT_STATUS_NORMAL);
        account.setModify_staff(adminId);
        account.setModify_time(timeNow);
        // 解冻资金
        balance.setModify_staff(adminId);
        balance.setModify_time(timeNow);
        balance.setStatus(Constant.BALANCE_STATUS_NORMAL);

        // 更新数据库
        // todo 证券账户所有证券解冻
        try {
            accountTransaction.saveAccountAndBalanceDB(account, balance);
        } catch (Exception e) {
            log.error("ERROR REISSUE. ERROR:" + e.getMessage());
            throw new ServiceException(ErrorEnum.ERROR_UNKNOWN);
        }
    }

    public void cancel(long securitiesAccountId, long accountId, long adminId)throws ServiceException{
        // 检查账户安全
        Account account = accountDb.getAccountByIdAndRemoved(accountId, false);
        Balance balance = balanceDb.getBalanceByFundAccountIdAndRemoved(accountId, false);
        validateAccountAndBalance(account, balance);
        // 检查是否匹配
        if(account.getSecuritiesAccountId() != securitiesAccountId){
            throw new ServiceException(ErrorEnum.ERROR_SECURITIES_ACCOUNT_NOT_MATCH_FUND_ACCOUNT);
        }
        // 检查资金余额
        if(balance.getBalance() != 0 || balance.getAvailable_balance() != 0){
            throw new ServiceException(ErrorEnum.ERROR_BALANCE_IS_NOT_ZERO);
        }
        List<Record> records = recordDb.getAllByBalanceIdAndRemoved(balance.getId(), false);
        // remove account balance and record
        Date timeNow = new Date();
        account.setRemoved(true);
        account.setModify_time(timeNow);
        account.setModify_staff(adminId);
        balance.setRemoved(true);
        balance.setModify_time(timeNow);
        balance.setModify_staff(adminId);
        if(records != null){
            for(Record record : records){
                record.setModify_time(timeNow);
                record.setModify_staff(adminId);
                record.setRemoved(true);
            }
        }
        // todo 证券账户所有证券解冻
        try {
            accountTransaction.removeDb(account, balance, records);
        } catch (Exception e) {
            log.error("ERROR CANCEL. ERROR:" + e.getMessage());
            throw new ServiceException(ErrorEnum.ERROR_UNKNOWN);
        }
    }

    private void checkRecordHandled(long recordId) throws ServiceException{
        Record record = recordDb.getRecordByPreDecreaseIdAndRemoved(recordId, false);
        if( record != null && record.getOperate_code() == Constant.BALANCE_OPCODE_RECOVER){
            throw new ServiceException(ErrorEnum.ERROR_ALREADY_RECOVERED);
        } else if(record != null && record.getOperate_code() == Constant.BALANCE_OPCODE_REDUCE){
            throw new ServiceException(ErrorEnum.ERROR_ALREADY_DECREASED);
        } else if(record != null){
            throw new ServiceException(ErrorEnum.ERROR_UNKNOWN);
        }
    }

    private void checkPreDecreaseRecord(Record record) throws ServiceException{
        if(record == null){
            throw new ServiceException(ErrorEnum.ERROR_RECORD_NOT_FOUND);
        }
        if(record.getOperate_code() != Constant.BALANCE_OPCODE_BUY){
            throw new ServiceException(ErrorEnum.ERROR_RECORD_TYPE_ERROR);
        }
        checkRecordHandled(record.getId());
    }

    // 股票指令发出时，冻结/预扣除
    public long freeze(double amount, long accountId) throws ServiceException{
        if (amount <= 0) {
            throw new ServiceException(ErrorEnum.ERROR_AMOUNT_ERROR);
        }

        Account account = accountDb.getAccountByIdAndRemoved(accountId, false);
        Balance balance = balanceDb.getBalanceByFundAccountIdAndRemoved(accountId, false);
        validateAccountAndBalance(account, balance);
        checkBalance(balance, amount);

        // modify
        Date timeNow = new Date();
        balance.setAvailable_balance(balance.getAvailable_balance() - amount);
        balance.setModify_staff(0);
        balance.setModify_time(timeNow);

        // Record
        Record record = new Record();
        record.setBalanceId(balance.getId());
        record.setAmount(amount);
        record.setOperate_code(Constant.BALANCE_OPCODE_BUY);
        record.setPreDecreaseId(0);
        record.setCreate_staff(0);
        record.setCreate_time(timeNow);
        record.setModify_staff(0);
        record.setModify_time(timeNow);
        record.setRemoved(false);
        try{
            accountTransaction.saveBalanceAndRecordDB(balance, record);
        } catch (Exception e){
            log.error("ERROR FREEZE BALANCE. ERROR: " + e.getMessage());
            throw new ServiceException(ErrorEnum.ERROR_UNKNOWN);
        }
        return record.getId();
    }

    // 股票购买成功时，扣除
    public void decrease(long recordId) throws ServiceException{
        Record record = recordDb.getRecordByIdAndRemoved(recordId, false);
        checkPreDecreaseRecord(record);

        // get balance
        Balance balance = balanceDb.getBalanceByIdAndRemoved(record.getBalanceId(), false);
        if(balance == null){
            throw new ServiceException(ErrorEnum.ERROR_BALANCE_NOT_EXIST);
        }

        // update
        Date timeNow = new Date();
        balance.setBalance(balance.getBalance() - record.getAmount());
        balance.setModify_staff(0);
        balance.setModify_time(timeNow);

        // Record
        Record r = new Record();
        r.setBalanceId(balance.getId());
        r.setAmount(record.getAmount());
        r.setOperate_code(Constant.BALANCE_OPCODE_REDUCE);
        r.setPreDecreaseId(recordId);
        r.setCreate_staff(0);
        r.setCreate_time(timeNow);
        r.setModify_staff(0);
        r.setModify_time(timeNow);
        r.setRemoved(false);
        try{
            accountTransaction.saveBalanceAndRecordDB(balance, r);
        } catch (Exception e){
            log.error("ERROR DECREASE BALANCE. ERROR: " + e.getMessage());
            throw new ServiceException(ErrorEnum.ERROR_UNKNOWN);
        }
    }

    // 股票购买失败/指令撤回时，恢复
    public void recover(long recordId) throws ServiceException{
        Record record = recordDb.getRecordByIdAndRemoved(recordId, false);
        checkPreDecreaseRecord(record);

        // get balance
        Balance balance = balanceDb.getBalanceByIdAndRemoved(record.getBalanceId(), false);
        if(balance == null){
            throw new ServiceException(ErrorEnum.ERROR_BALANCE_NOT_EXIST);
        }

        // update
        Date timeNow = new Date();
        balance.setAvailable_balance(balance.getAvailable_balance() + record.getAmount());
        balance.setModify_staff(0);
        balance.setModify_time(timeNow);

        // Record
        Record r = new Record();
        r.setBalanceId(balance.getId());
        r.setAmount(record.getAmount());
        r.setOperate_code(Constant.BALANCE_OPCODE_RECOVER);
        r.setPreDecreaseId(recordId);
        r.setCreate_staff(0);
        r.setCreate_time(timeNow);
        r.setModify_staff(0);
        r.setModify_time(timeNow);
        r.setRemoved(false);
        try{
            accountTransaction.saveBalanceAndRecordDB(balance, r);
        } catch (Exception e){
            log.error("ERROR RECOVER BALANCE. ERROR: " + e.getMessage());
            throw new ServiceException(ErrorEnum.ERROR_UNKNOWN);
        }
    }

    // 交易客户端登陆
    public void clientLogin(long accountId, String password) throws ServiceException{
        Account account = accountDb.getAccountByIdAndRemoved(accountId, false);
        if(account == null){
            throw new ServiceException(ErrorEnum.ERROR_ACCOUNT_NOT_EXIST);
        }
        if(!account.getTransaction_pwd().equals(password)){
            throw new ServiceException(ErrorEnum.ERROR_USERNAME_PASSWORD_ERROR);
        }
    }

}
