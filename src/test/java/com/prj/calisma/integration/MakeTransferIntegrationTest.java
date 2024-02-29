package com.prj.calisma.integration;

import com.prj.calisma.controllers.TransactionRestController;
import com.prj.calisma.utils.AccountInput;
import com.prj.calisma.utils.TransactionInput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = "local")
class MakeTransferIntegrationTest {

    @Autowired
    private TransactionRestController transactionRestController;

    @Test
    void givenTransactionDetails_whenMakeTransaction_thenVerifyTransactionIsProcessed() {
        var sourceAccount = new AccountInput();
        sourceAccount.setSortCode("82-53-56");
        sourceAccount.setAccountNumber("70002363");

        var targetAccount = new AccountInput();
        targetAccount.setSortCode("72-19-20");
        targetAccount.setAccountNumber("72503139");

        var input = new TransactionInput();
        input.setSourceAccount(sourceAccount);
        input.setTargetAccount(targetAccount);
        input.setAmount(55.0);
        input.setReference("Açıklama");

        var body = transactionRestController.makeTransfer(input).getBody();

        var isComplete = (Boolean) body;
        assertThat(isComplete).isTrue();
    }
}
