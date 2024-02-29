package com.prj.calisma.unit;

import com.prj.calisma.models.Account;
import com.prj.calisma.repositories.AccountRepository;
import com.prj.calisma.repositories.TransactionRepository;
import com.prj.calisma.services.TransactionService;
import com.prj.calisma.utils.AccountInput;
import com.prj.calisma.utils.TransactionInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class TransactionServiceTest {

    @TestConfiguration
    static class TransactionServiceTestContextConfiguration {

        @Bean
        public TransactionService transactionService() {
            return new TransactionService();
        }
    }

    @Autowired
    private TransactionService transactionService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        var sourceAccount = new Account(1L, "53-68-92", "50002363", 458.1, "Some Bank", "John", 0.0, 0.0);
        var targetAccount = new Account(2L, "67-41-18", "22503139", 64.9, "Some Other Bank", "Major", 0.0, 0.0);

        when(accountRepository.findBySortCodeAndAccountNumber("53-68-92", "78901234"))
                .thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findBySortCodeAndAccountNumber("67-41-18", "48573590"))
                .thenReturn(Optional.of(targetAccount));
    }

    @Test
    void whenTransactionDetails_thenTransferShouldBeDenied() {
        var sourceAccount = new AccountInput();
        sourceAccount.setSortCode("53-68-92");
        sourceAccount.setAccountNumber("50002363");

        var targetAccount = new AccountInput();
        targetAccount.setSortCode("67-41-19");
        targetAccount.setAccountNumber("22503139");

        var input = new TransactionInput();
        input.setSourceAccount(sourceAccount);
        input.setTargetAccount(targetAccount);
        input.setAmount(50);
        input.setReference("Açıklama");

        boolean isComplete = transactionService.makeTransfer(input);

        assertThat(isComplete).isTrue();
    }

    @Test
    void whenTransactionDetailsAndAmountTooLarge_thenTransferShouldBeDenied() {
        var sourceAccount = new AccountInput();
        sourceAccount.setSortCode("53-68-92");
        sourceAccount.setAccountNumber("50002363");

        var targetAccount = new AccountInput();
        targetAccount.setSortCode("67-41-18");
        targetAccount.setAccountNumber("22503139");

        var input = new TransactionInput();
        input.setSourceAccount(sourceAccount);
        input.setTargetAccount(targetAccount);
        input.setAmount(1000000000);
        input.setReference("Açıklama");

        boolean isComplete = transactionService.makeTransfer(input);

        assertThat(isComplete).isFalse();
    }
}
