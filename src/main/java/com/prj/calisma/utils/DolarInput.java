package com.prj.calisma.utils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.Objects;

public class DolarInput {

    @NotBlank(message = "Gönderilecek hesap numarası boş bırakılamaz.")
    private String sourceAccountNo;

    @Positive(message = "Gönderilen miktar pozitif sayı olmalıdır.")
    private double amount;

    public DolarInput() {
    }

    public String getSourceAccountNo() {
        return sourceAccountNo;
    }

    public void setSourceAccountNo(String sourceAccountNo) {
        this.sourceAccountNo = sourceAccountNo;
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
                "targetAccountNo='" + sourceAccountNo + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DolarInput that = (DolarInput) o;
        return Objects.equals(sourceAccountNo, that.sourceAccountNo) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceAccountNo, amount);
    }
}
