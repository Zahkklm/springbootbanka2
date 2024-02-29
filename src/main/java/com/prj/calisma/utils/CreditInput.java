package com.prj.calisma.utils;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class CreditInput {

    private String ownerAccountId;
    private String action; // ÖDEME veya KREDİ olduğunu belli etmeli

    private double creditAmount;

    public CreditInput() {}

    public String getOwnerAccountId() {
        return ownerAccountId;
    }

    public void setOwnerAccountId(String ownerAccountId) {
        this.ownerAccountId = ownerAccountId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public double getCreditAmount(){
        return creditAmount;
    }

    public void setCreditAmount(double creditAmount){
        this.creditAmount = creditAmount;
    }

    // Döndürülen string test ettir
    @Override
    public String toString() {
        return "CreditInput{" +
                "ownerAccountId='" + ownerAccountId + '\'' +
                ", paymentorwithdraw='" + action + '\'' + // 'ÖDEME' yazılırsa borç ödenir limit artar, 'KREDİ' yazarsa tam tersi
                ", creditAmount='" + creditAmount + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditInput that = (CreditInput) o;
        return Objects.equals(ownerAccountId, that.ownerAccountId) &&
                Objects.equals(action, that.action);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerAccountId, action);
    }
}
