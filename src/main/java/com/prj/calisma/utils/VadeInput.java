package com.prj.calisma.utils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.Objects;

public class VadeInput {

    @NotBlank(message = "Hesap no boş bırakılamaz.")
    private String targetAccountNo;

    @Positive(message = "Gönderilecek miktar pozitif olmalıdır.")
    private double amount;
    private int vade;

    public VadeInput() {
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
    public int getVade(){
        return vade;
    }
    public void setVade(int vade){
        this.vade = vade;
    }

    @Override
    public String toString() {
        return "VadeInput{" +
                "targetAccountNo='" + targetAccountNo + '\'' +
                ", amount='" + amount + '\'' +
                ", vade='" + vade + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VadeInput that = (VadeInput) o;
        return Objects.equals(targetAccountNo, that.targetAccountNo) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetAccountNo, amount);
    }
}
