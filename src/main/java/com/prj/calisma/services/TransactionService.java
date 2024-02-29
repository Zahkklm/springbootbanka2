package com.prj.calisma.services;

import com.google.zxing.WriterException;
import com.prj.calisma.utils.CodeGenerator;
import com.prj.calisma.utils.QrCodeGenerator;
import com.prj.calisma.constants.ACTION;
import com.prj.calisma.models.Account;
import com.prj.calisma.models.Transaction;
import com.prj.calisma.repositories.AccountRepository;
import com.prj.calisma.repositories.TransactionRepository;
import com.prj.calisma.utils.TransactionInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.Base64;

import java.io.IOException;
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
    private Object scheduler;

    public boolean makeTransfer(TransactionInput transactionInput) {
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
                transaction.setDolar(0.0);
                transaction.setCreditAmount(0.0);                

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
                transaction2.setCreditAmount(0.0);
                transaction.setDolar(0.0);
                transactionRepository2.save(transaction2);

                // İleride QR kod yazdırma ekle
                getQRCode(sourceAccount.get().getOwnerName());
                
                return true;
            }

            // Bakiye yetersizse ve kredisi varsa krediden ödeme yapılsın
            else if (isCreditAvailable(sourceAccount.get(), transactionInput.getAmount()))  {
                var transaction = new Transaction();

                var date = LocalDateTime.now();
                var datelast = LocalDateTime.now();

                var sourceAccountId = sourceAccount.get().getId();
                var targetAccountId = targetAccount.get().getId();

                var creditAmount = sourceAccount.get().getCreditAmount();

                transaction.setAmount(transactionInput.getAmount());
                transaction.setSourceAccountId(sourceAccountId);
                transaction.setTargetAccountId(targetAccountId);
                transaction.setTargetOwnerName(targetAccount.get().getOwnerName());
                transaction.setInitiationDate(date);
                transaction.setCompletionDate(datelast);
                transaction.setReference(transactionInput.getReference());
                transaction.setCreditAmount(creditAmount);
                transaction.setDolar(0.0);

                // Gönderen hesaptan çek
                updateAccountCredit(sourceAccount.get(), transactionInput.getAmount(), ACTION.CREDIT);            
          
                // Hedef hesaba gönder
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
                transaction2.setCreditAmount(0.0);
                transaction2.setDolar(0.0);
                transactionRepository2.save(transaction2);

                // İleride QR kod yazdırma ekle
                getQRCode(sourceAccount.get().getOwnerName());
                return true;
            }
        }
        return false;
    }

    public String getQRCode(String veri){

        byte[] image = new byte[0];
        try {
            image = QrCodeGenerator.getQRCodeImage("ads",250,250);
            final String QR_CODE_IMAGE_PATH = "C:\\img\\QRCode.png";
            System.out.println(QR_CODE_IMAGE_PATH);
            QrCodeGenerator.generateQRCodeImage(veri,250,250,QR_CODE_IMAGE_PATH);

        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
        // Byte Dizisi -> String dönüştürme
        String qrcode = Base64.getEncoder().encodeToString(image);
        return qrcode;
    }
/* 
    @Scheduled(cron = "0 0 0 1 * * *")
    public void updateVade(Account account, double amount){
        account.setCurrentBalance((account.getCurrentBalance() + amount));
    }
*/

    public void updateAccountBalance(Account account, double amount, ACTION action) {
        if (action == ACTION.WITHDRAW) {
            account.setCurrentBalance((account.getCurrentBalance() - amount));
        } else if (action == ACTION.DEPOSIT) {
            account.setCurrentBalance((account.getCurrentBalance() + amount));
        }        
        accountRepository.save(account);
    }

    public boolean isAmountAvailable(double amount, double accountBalance) {
        return (accountBalance - amount) > 0;
    }

    public void updateDolar(Account account, double amount, ACTION action) {
        if (action == ACTION.WITHDRAW) {
            account.setDolar((account.getDolar() - amount));
        } else if (action == ACTION.DEPOSIT) {
            account.setDolar((account.getDolar() + amount));
        }
        accountRepository.save(account);
    }

    public void updateAccountCredit(Account account, double amount, ACTION action) {
        if (action == ACTION.DEBTPAY) { // Kredi borcu ödendiğinde
            account.setCreditAmount((account.getCreditAmount() + amount));
        } else if (action == ACTION.CREDIT) { // Kredi limitinden harcama yapıldığında
            account.setCreditAmount((account.getCreditAmount() - amount));
        }
        accountRepository.save(account);
    }

    public boolean isCreditAvailable(Account account, double amount){
        double credit = account.getCreditAmount();
        if(credit - amount >= 0){
           return true;
        } 

        return false;
    }
}
