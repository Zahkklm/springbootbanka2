package com.prj.calisma.utils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

public class TransactionInput {

    private AccountInput sourceAccount;

    private AccountInput targetAccount;

    @Positive(message = "Gönderilen miktar pozitif sayı olmalıdır.")
    @Min(value = 1, message = "Miktar 1'den büyük olmalıdır.")

    private double amount;

    private String reference;

    public TransactionInput() {}

    public AccountInput getSourceAccount() {
        return sourceAccount;
    }
    public void setSourceAccount(AccountInput sourceAccount) {
        this.sourceAccount = sourceAccount;
    }
    public AccountInput getTargetAccount() {
        return targetAccount;
    }
    public void setTargetAccount(AccountInput targetAccount) {
        this.targetAccount = targetAccount;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public String getReference() {
        return reference;
    }
    public void setReference(String reference) {
        this.reference = reference;
    }


    @Override
    public String toString() {
        return "TransactionInput{" +
                "sourceAccount=" + sourceAccount +
                ", targetAccount=" + targetAccount +
                ", amount=" + amount +
                ", reference='" + reference + 
                '}';
    }
}
