package com.prj.calisma.services;

import com.prj.calisma.constants.ACTION;
import com.prj.calisma.models.Account;
import com.prj.calisma.models.Transaction;
import com.prj.calisma.repositories.AccountRepository;
import com.prj.calisma.repositories.TransactionRepository;
import com.prj.calisma.utils.TransactionInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionRepository transactionRepository2;

    public boolean makeTransfer(TransactionInput transactionInput) {
        // TODO refactor synchronous implementation with messaging queue
        String sourceSortCode = transactionInput.getSourceAccount().getSortCode();
        String sourceAccountNumber = transactionInput.getSourceAccount().getAccountNumber();
        Optional<Account> sourceAccount = accountRepository
                .findBySortCodeAndAccountNumber(sourceSortCode, sourceAccountNumber);

        String targetSortCode = transactionInput.getTargetAccount().getSortCode();
        String targetAccountNumber = transactionInput.getTargetAccount().getAccountNumber();
        Optional<Account> targetAccount = accountRepository
                .findBySortCodeAndAccountNumber(targetSortCode, targetAccountNumber);

        if (sourceAccount.isPresent() && targetAccount.isPresent()) {
            if (isAmountAvailable(transactionInput.getAmount(), sourceAccount.get().getCurrentBalance())) {
                var transaction = new Transaction();

                var date = LocalDateTime.now();
                var datelast = LocalDateTime.now();

                var sourceAccountId = sourceAccount.get().getId();
                var targetAccountId = targetAccount.get().getId();

                transaction.setAmount(transactionInput.getAmount());
                transaction.setSourceAccountId(sourceAccountId);
                transaction.setTargetAccountId(targetAccountId);
                transaction.setTargetOwnerName(targetAccount.get().getOwnerName());
                transaction.setInitiationDate(date);
                transaction.setCompletionDate(datelast);
                transaction.setReference(transactionInput.getReference());
                transaction.setLatitude(transactionInput.getLatitude());
                transaction.setLongitude(transactionInput.getLongitude());

                updateAccountBalance(sourceAccount.get(), transactionInput.getAmount(), ACTION.WITHDRAW);            
          
                updateAccountBalance(targetAccount.get(), transactionInput.getAmount(), ACTION.DEPOSIT);

                transactionRepository.save(transaction);

                var transaction2 = new Transaction();

                transaction2.setAmount(transactionInput.getAmount());
                transaction2.setSourceAccountId(sourceAccountId);
                transaction2.setTargetAccountId(targetAccountId);
                transaction2.setTargetOwnerName(sourceAccount.get().getOwnerName());
                transaction2.setInitiationDate(date);
                transaction2.setCompletionDate(datelast);
                transaction2.setReference(transactionInput.getReference());
                transaction2.setLatitude(transactionInput.getLatitude());
                transaction2.setLongitude(transactionInput.getLongitude());
                transactionRepository2.save(transaction2);

                return true;
            }
        }
        return false;
    }

     public void updateAccountBalance(Account account, double amount, ACTION action) {
        if (action == ACTION.WITHDRAW) {
            account.setCurrentBalance((account.getCurrentBalance() - amount));
        } else if (action == ACTION.DEPOSIT) {
            account.setCurrentBalance((account.getCurrentBalance() + amount));
        }
        accountRepository.save(account);
    }

    // TODO support overdrafts or credit account
    public boolean isAmountAvailable(double amount, double accountBalance) {
        return (accountBalance - amount) > 0;
    }
}
