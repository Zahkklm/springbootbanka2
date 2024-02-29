package com.prj.calisma.controllers;

import com.prj.calisma.constants.ACTION;
import com.prj.calisma.models.Account;
import com.prj.calisma.services.AccountService;
import com.prj.calisma.services.TransactionService;
import com.prj.calisma.utils.*;

import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static com.prj.calisma.constants.constants.*;


@RestController
@RequestMapping("api/v1")

public class TransactionRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionRestController.class);

    private final AccountService accountService;
    private final TransactionService transactionService;

    @Autowired
    public TransactionRestController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @PostMapping(value = "/transactions",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> makeTransfer(
            @Valid @RequestBody TransactionInput transactionInput) {
        if (InputValidator.isSearchTransactionValid(transactionInput)) {
            boolean isComplete = transactionService.makeTransfer(transactionInput);
            return new ResponseEntity<>(isComplete, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(INVALID_TRANSACTION, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/withdraw",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> withdraw(
            @Valid @RequestBody WithdrawInput withdrawInput) {
        LOGGER.debug("ÇALIŞTIRILDI: AccountRestController.withdrawInput");

        if (InputValidator.isSearchCriteriaValid(withdrawInput)) {
            Account account = accountService.getAccount(
                    withdrawInput.getSortCode(), withdrawInput.getAccountNumber());

            if (account == null) {
                return new ResponseEntity<>(NO_ACCOUNT_FOUND, HttpStatus.OK);
            } else {
                if (transactionService.isAmountAvailable(withdrawInput.getAmount(), account.getCurrentBalance())) {
                    transactionService.updateAccountBalance(account, withdrawInput.getAmount(), ACTION.WITHDRAW);
                    return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
                }
                return new ResponseEntity<>(INSUFFICIENT_ACCOUNT_BALANCE, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(INVALID_SEARCH_CRITERIA, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(value = "/deposit",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deposit(
            @Valid @RequestBody DepositInput depositInput) {
        LOGGER.debug("ÇALIŞTIRILDI: AccountRestController.depositInput");

        if (InputValidator.isAccountNoValid(depositInput.getTargetAccountNo())) {
            Account account = accountService.getAccount(depositInput.getTargetAccountNo());

            if (account == null) {
                return new ResponseEntity<>(NO_ACCOUNT_FOUND, HttpStatus.OK);
            } else {
                transactionService.updateAccountBalance(account, depositInput.getAmount(), ACTION.DEPOSIT);
                return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(INVALID_SEARCH_CRITERIA, HttpStatus.BAD_REQUEST);
        }
    }
   
    @PostMapping(value = "/vade",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> vade(
        @Valid @RequestBody VadeInput vadeInput) {
        LOGGER.debug("ÇALIŞTIRILDI: AccountRestController.VadeInput");
        // Girdi doğrulaması
        if (InputValidator.isAccountNoValid(vadeInput.getTargetAccountNo())) {
            Account account = accountService.getAccount(vadeInput.getTargetAccountNo());
            // Hesap mevcutsa hesap bilgilerini döndür
            if (account == null) {
                return new ResponseEntity<>(NO_ACCOUNT_FOUND, HttpStatus.OK);
            } 
            else {
                if(transactionService.isAmountAvailable(vadeInput.getAmount(), account.getCurrentBalance())){
                    // Yeterli miktarda parası varsa olacaklar
                    transactionService.updateAccountBalance(account, vadeInput.getVade(), ACTION.VADE);
                    // TODO Mevduat İşlemleri: her ay faiz yatırılsın
                    
                    
                    return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
                }                
                return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(INVALID_SEARCH_CRITERIA, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/credit",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> credit(
            @Valid @RequestBody CreditInput creditInput) {
        LOGGER.debug("ÇALIŞTIRILDI: AccountRestController.creditInput");

        // Girdi doğrulaması
        if (
            InputValidator.isAccountNoValid(creditInput.getOwnerAccountId())         
               ) {
            Account account = accountService.getAccount(
               creditInput.getOwnerAccountId()
                );

            if (account == null) {
                return new ResponseEntity<>(NO_ACCOUNT_FOUND, HttpStatus.OK);
            } else {
                transactionService.updateAccountBalance(account, creditInput.getCreditAmount(), ACTION.DEPOSIT);
                transactionService.updateAccountCredit(account, -creditInput.getCreditAmount(), ACTION.CREDIT);
                return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(INVALID_SEARCH_CRITERIA, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/dolar",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> dolar(
            @Valid @RequestBody DolarInput dolarInput) {
        LOGGER.debug("ÇALIŞTIRILDI: AccountRestController.dolarInput");

        // Girdi doğrulaması
        if (InputValidator.isAccountNoValid(dolarInput.getSourceAccountNo())) {
            Account account = accountService.getAccount(
                    dolarInput.getSourceAccountNo());

            if (account == null) {
                return new ResponseEntity<>(NO_ACCOUNT_FOUND, HttpStatus.OK);
            } else {
                if (transactionService.isAmountAvailable(dolarInput.getAmount(), account.getCurrentBalance())) {
                    transactionService.updateAccountBalance(account, dolarInput.getAmount(), ACTION.WITHDRAW); // TL olarak çek
                    
                    final double DOLAR_KUR = 0.037; // 12.08.2023 TL-DOLAR kuru
                    transactionService.updateDolar(account, dolarInput.getAmount() * DOLAR_KUR, ACTION.DEPOSIT); // DOLAR olarak yatır
                    return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
                }
                return new ResponseEntity<>(INSUFFICIENT_ACCOUNT_BALANCE, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(INVALID_SEARCH_CRITERIA, HttpStatus.BAD_REQUEST);
        }
    }
    

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }
}
