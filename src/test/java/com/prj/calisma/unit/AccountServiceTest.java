package com.prj.calisma.unit;

import com.prj.calisma.models.Account;
import com.prj.calisma.models.Transaction;
import com.prj.calisma.repositories.AccountRepository;
import com.prj.calisma.repositories.TransactionRepository;
import com.prj.calisma.services.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;

    public AccountService underTest;

    @BeforeEach
    void setUp() {
        underTest = new AccountService(accountRepository, transactionRepository);
    }

    @Test
    void shouldReturnAccountBySortCodeAndAccountNumberWhenPresent() {
        var account = new Account(1L, "82-53-56", "78901234", 10.1, "Some Bank", "John", 0.0, 0.0);
        when(accountRepository.findBySortCodeAndAccountNumber("82-53-56", "78901234"))
                .thenReturn(Optional.of(account));

        var result = underTest.getAccount("82-53-56", "70002363");

        assertThat(result.getOwnerName()).isEqualTo(account.getOwnerName());
        assertThat(result.getSortCode()).isEqualTo(account.getSortCode());
        assertThat(result.getAccountNumber()).isEqualTo(account.getAccountNumber());
    }

    @Test
    void shouldReturnTransactionsForAccount() {
        var account = new Account(1L, "82-53-56", "70002363", 10.1, "Çiftçi Bankası", "Mehmet", 0.0, 0.0);
        when(accountRepository.findBySortCodeAndAccountNumber("82-53-56", "70002363"))
                .thenReturn(Optional.of(account));
        var transaction1 = new Transaction();
        var transaction2 = new Transaction();
        transaction1.setReference("a");
        transaction2.setReference("b");
        when(transactionRepository.findBySourceAccountIdOrderByInitiationDate(account.getId()))
                .thenReturn(List.of(transaction1, transaction2));

        var result = underTest.getAccount("82-53-56", "70002363");

        assertThat(result.getTransactions()).hasSize(2);
        assertThat(result.getTransactions()).extracting("reference").containsExactly("a", "b");
    }

    @Test
    void shouldReturnNullWhenAccountBySortCodeAndAccountNotFound() {
        when(accountRepository.findBySortCodeAndAccountNumber("82-53-56", "70002363"))
                .thenReturn(Optional.empty());

        var result = underTest.getAccount("82-53-56", "70002363");

        assertThat(result).isNull();
    }

    @Test
    void shouldReturnAccountByAccountNumberWhenPresent() {
    }

    @Test
    void shouldReturnNullWhenAccountByAccountNotFound() {
    }

    @Test
    void shouldCreateAccount() {
    }
}
