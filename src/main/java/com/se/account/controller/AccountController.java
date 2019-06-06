package com.se.account.controller;

import com.se.account.dal.AccountRepository;
import com.se.account.dal.BalanceRepository;
import com.se.account.domain.Account;
import com.se.account.domain.Balance;
import com.se.account.dto.AccountInfoDTO;
import com.se.account.dto.CreateAccountRespDTO;
import com.se.account.service.AccountService;
import com.se.account.util.Check;
import com.se.account.util.Constant;
import com.se.account.util.ErrorEnum;
import com.se.account.util.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Date;

@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController extends BaseController{
    // todo 每年定时发放利息
    @Resource
    AccountService accountService;

    @Resource
    AccountRepository accountDb;

    @Resource
    BalanceRepository balanceDb;

    /*
    *   创建资金账户，返回资金账户的id
    *   @param  identityId
    *   @param  securitiesAccountId
    *   @param  transactionPwd
    *   @param  withdrawalPwd
    *   @return accountId
    * */
    @RequestMapping("/create")
    public Object create(long identityId, long securitiesAccountId,
                         @NotNull String transactionPwd, @NotNull String withdrawalPwd, int currency){
        // todo check match identityId and securitiesAccountId
        if(accountDb.getAccountBySecuritiesAccountIdAndRemoved(securitiesAccountId, false) != null){
            return buildResponse(ErrorEnum.ERROR_SECURITIES_ACCOUNT_HAS_FUND_ACCOUNT);
        }

        if(!Check.checkValidPassword(transactionPwd) || !Check.checkValidPassword(withdrawalPwd)){
            log.error("Invalid password: " + transactionPwd + ":" + withdrawalPwd);
            return buildResponse(ErrorEnum.ERROR_PASSWORD_INVALID);
        }

        // check support curency
        if(!Check.checkSupportCurrency(currency)){
            log.error("Unsupport currency : " + currency);
            return buildResponse(ErrorEnum.ERROR_UNSUPPORT_CURRENCY);
        }

        long accountId = 0;
        try {
            accountId = accountService.create(securitiesAccountId, transactionPwd, withdrawalPwd, currency, getAdmin().getId());
        } catch (ServiceException e){
            log.error("Create found account error: " + e.getErrorEnum().getEnDes());
            return buildResponse(e.getErrorEnum());
        }
        if(accountId == 0){
            return buildResponse(ErrorEnum.ERROR_UNKNOWN);
        }
        CreateAccountRespDTO createAccountRespDTO = new CreateAccountRespDTO();
        createAccountRespDTO.setAccountId(accountId);
        return buildSuccessResp(createAccountRespDTO);
    }

    /*
     *   创建资金账户，返回资金账户的id
     *   @return accountInfoDTO
     * */
    @RequestMapping("/info")
    public Object info(long accountId){
        Account account = accountDb.getAccountByIdAndRemoved(accountId, false);
        if(account == null){
            return buildResponse(ErrorEnum.ERROR_ACCOUNT_NOT_EXIST);
        }
        Balance balance = balanceDb.getBalanceByFundAccountIdAndRemoved(accountId, false);
        if(balance == null){
            return buildResponse(ErrorEnum.ERROR_BALANCE_NOT_EXIST);
        }

        // balance info
        AccountInfoDTO.BalanceInfoDTO balanceInfoDTO = new AccountInfoDTO.BalanceInfoDTO();
        balanceInfoDTO.setId(balance.getId());
        balanceInfoDTO.setCurrency(balance.getCurrency());
        balanceInfoDTO.setAvailable_balance(balance.getAvailable_balance());
        balanceInfoDTO.setBalance(balance.getBalance());
        balanceInfoDTO.setCreate_staff(balance.getCreate_staff());
        balanceInfoDTO.setCreate_time(balance.getCreate_time());
        balanceInfoDTO.setModify_staff(balance.getModify_staff());
        balanceInfoDTO.setModify_time(balance.getModify_time());
        balanceInfoDTO.setStatus(balance.getStatus());

        // Account Info
        AccountInfoDTO accountInfoDTO = new AccountInfoDTO();
        accountInfoDTO.setId(accountId);
        accountInfoDTO.setSecurities_account_id(account.getSecuritiesAccountId());
        accountInfoDTO.setStatus(account.getStatus());
        accountInfoDTO.setBalanceInfo(balanceInfoDTO);
        accountInfoDTO.setCreate_staff(account.getCreate_staff());
        accountInfoDTO.setCreate_time(account.getCreate_time());
        accountInfoDTO.setModify_staff(account.getModify_staff());
        accountInfoDTO.setModify_time(account.getModify_time());

        return buildSuccessResp(accountInfoDTO);
    }


    /*
     *   创建资金账户，返回是否成功
     *   @param  accountId
     *   @param  amount
     *   @param  password
     *   @return
     * */
    @RequestMapping("/save")
    public Object save(long accountId, String password, double amount){
        try {
            accountService.operate(accountId, password, amount, getAdmin().getId(), Constant.BALANCE_OPCODE_SAVE);
        }catch (ServiceException e){
            log.error("Save balance error: " + e.getErrorEnum().getEnDes());
            return buildResponse(e.getErrorEnum());
        }
        return buildSuccessResp(null);
    }

    @RequestMapping("/withdrawal")
    public Object withdrawal(long accountId, String password, double amount){
        try {
            accountService.operate(accountId, password, amount, getAdmin().getId(), Constant.BALANCE_OPCODE_WITHDRAWAL);
        }catch (ServiceException e){
            log.error("Withdrawal balance error: " + e.getErrorEnum().getEnDes());
            return buildResponse(e.getErrorEnum());
        }
        return buildSuccessResp(null);
    }

    @RequestMapping("/updateTransactionPwd")
    public Object updateTransactionPwd(long accountId, @NotNull String oriPassword,@NotNull String newPassword){
        try {
            Account account = accountDb.getAccountByIdAndRemoved(accountId, false);
            accountService.validateAccount(account);
            if(!oriPassword.equals(account.getTransaction_pwd())){
                throw new ServiceException(ErrorEnum.ERROR_PASSWORD_NOT_MATCH);
            }
            if(!Check.checkValidPassword(newPassword)){
                throw new ServiceException(ErrorEnum.ERROR_PASSWORD_INVALID);
            }

            // save password
            account.setTransaction_pwd(newPassword);
            account.setModify_staff(getAdmin().getId());
            account.setModify_time(new Date());
            try {
                accountDb.save(account);
            } catch (Exception e){
                log.error("DATABASE ERROR: " + e.getMessage());
                throw new ServiceException(ErrorEnum.ERROR_UNKNOWN);
            }
        }catch (ServiceException e){
            log.error("Update transaction password error: " + e.getErrorEnum().getEnDes());
            return buildResponse(e.getErrorEnum());
        }
        return buildSuccessResp(null);
    }

    @RequestMapping("/updateWithdrawalPwd")
    public Object updateWithdrawalPwd(long accountId, @NotNull String oriPassword,@NotNull String newPassword){
        try {
            Account account = accountDb.getAccountByIdAndRemoved(accountId, false);
            accountService.validateAccount(account);
            if(!oriPassword.equals(account.getWithdrawal_pwd())){
                throw new ServiceException(ErrorEnum.ERROR_PASSWORD_NOT_MATCH);
            }
            if(!Check.checkValidPassword(newPassword)){
                throw new ServiceException(ErrorEnum.ERROR_PASSWORD_INVALID);
            }
            // save password
            account.setWithdrawal_pwd(newPassword);
            account.setModify_staff(getAdmin().getId());
            account.setModify_time(new Date());
            try {
                accountDb.save(account);
            } catch (Exception e){
                log.error("DATABASE ERROR: " + e.getMessage());
                throw new ServiceException(ErrorEnum.ERROR_UNKNOWN);
            }
        }catch (ServiceException e){
            log.error("Update withdrawal password error: " + e.getErrorEnum().getEnDes());
            return buildResponse(e.getErrorEnum());
        }
        return buildSuccessResp(null);
    }

    @RequestMapping("/reportLoss")
    public Object reportLoss(long identityId, long securitiesAccountId){
        // todo check match identityId and securitiesAccountId
        try {
            accountService.reportLoss(securitiesAccountId, getAdmin().getId());
        }catch (ServiceException e){
            log.error("Report Loss error: " + e.getErrorEnum().getEnDes());
            return buildResponse(e.getErrorEnum());
        }
        return buildSuccessResp(null);
    }

    @RequestMapping("/reissue")
    public Object reissue(long identityId, long securitiesAccountId,
                          @NotNull String transactionPwd, @NotNull String withdrawalPwd){
        // todo check match identityId and securitiesAccountId
        try {
            accountService.reissue(securitiesAccountId, transactionPwd, withdrawalPwd, getAdmin().getId());
            return buildSuccessResp(null);
        }catch (ServiceException e){
            log.error("Reissue error: " + e.getErrorEnum().getEnDes());
            return buildResponse(e.getErrorEnum());
        }
    }

    @RequestMapping("/cancel")
    public Object cancel(long identityId, long securitiesAccountId, long accountId){
        // todo check match identityId and securitiesAccountId
        try {
            accountService.cancel(securitiesAccountId, accountId, getAdmin().getId());
            return buildSuccessResp(null);
        }catch (ServiceException e){
            log.error("Cancel error: " + e.getErrorEnum().getEnDes());
            return buildResponse(e.getErrorEnum());
        }
    }

    @RequestMapping("/freeze")
    public Object freeze(double amount, long accountId){
        try {
            return buildSuccessResp(accountService.freeze(amount, accountId));
        }catch (ServiceException e){
            log.error("Freeze error: " + e.getErrorEnum().getEnDes());
            return buildResponse(e.getErrorEnum());
        }
    }

    @RequestMapping("/decrease")
    public Object freeze(long recordId){
        try {
            accountService.decrease(recordId);
            return buildSuccessResp(null);
        }catch (ServiceException e){
            log.error("Decrease error: " + e.getErrorEnum().getEnDes());
            return buildResponse(e.getErrorEnum());
        }
    }

    @RequestMapping("/recover")
    public Object recover(long recordId){
        try {
            accountService.recover(recordId);
            return buildSuccessResp(null);
        }catch (ServiceException e){
            log.error("Recover error: " + e.getErrorEnum().getEnDes());
            return buildResponse(e.getErrorEnum());
        }
    }

    @RequestMapping("/clientLogin")
    public Object clientLogin(long accountId, String password){
        try {
            accountService.clientLogin(accountId, password);
            return buildSuccessResp(null);
        }catch (ServiceException e){
            log.error("Client login error: " + e.getErrorEnum().getEnDes());
            return buildResponse(e.getErrorEnum());
        }
    }
}
