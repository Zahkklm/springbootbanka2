package com.prj.calisma.integration;

import com.prj.calisma.controllers.AccountRestController;
import com.prj.calisma.models.Account;
import com.prj.calisma.utils.AccountInput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = "local")
class CheckBalanceIntegrationTest {

    @Autowired
    private AccountRestController accountRestController;

    @Test
    void givenAccountDetails_whenCheckingBalance_thenVerifyAccountCorrect() {
        var input = new AccountInput();
        input.setSortCode("82-53-56");
        input.setAccountNumber("70002363");
        var body = accountRestController.checkAccountBalance(input).getBody();
        var account = (Account) body;
        assertThat(account).isNotNull();
        assertThat(account.getOwnerName()).isEqualTo("ÖzgürP");
        assertThat(account.getSortCode()).isEqualTo("82-53-56");
        assertThat(account.getAccountNumber()).isEqualTo("70002363");

        var input2 = new AccountInput();
        input.setSortCode("72-19-20");
        input.setAccountNumber("72503139");
        var body2 = accountRestController.checkAccountBalance(input).getBody();
        var account2 = (Account) body;
        assertThat(account).isNotNull();
        assertThat(account.getOwnerName()).isEqualTo("BarışP");
        assertThat(account.getSortCode()).isEqualTo("72-19-20");
        assertThat(account.getAccountNumber()).isEqualTo("72503139");
    }
}
