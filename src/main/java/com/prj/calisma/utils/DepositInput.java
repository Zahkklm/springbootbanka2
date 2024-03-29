package com.prj.calisma.utils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.Objects;

public class DepositInput {

    @NotBlank(message = "Gönderilecek hesap numarası boş bırakılamaz.")
    private String targetAccountNo;

    @Positive(message = "Gönderilen miktar pozitif sayı olmalıdır.")
    private double amount;

    public DepositInput() {
    }

    public String getTargetAccountNo() {
        return targetAccountNo;
    }

    public void setTargetAccountNo(String targetAccountNo) {
        this.targetAccountNo = targetAccountNo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "DepositInput{" +
                "targetAccountNo='" + targetAccountNo + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepositInput that = (DepositInput) o;
        return Objects.equals(targetAccountNo, that.targetAccountNo) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetAccountNo, amount);
    }
}
