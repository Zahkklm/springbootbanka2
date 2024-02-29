package com.prj.calisma.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "account", schema = "online_bank")

@SequenceGenerator(name = "account_seq", sequenceName = "account_sequence", schema = "online_bank", initialValue = 0)
public class Account {

    @Id 
    
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
    private long id;

    private String sortCode;

    private String accountNumber;

    private double currentBalance;

    private String bankName;

    private String ownerName;

    private double creditAmount;

    private double dolar;

    private transient List<Transaction> transactions;

    protected Account() {}
    public Account(String bankName, String ownerName, String generateSortCode, String generateAccountNumber, double currentBalance, double creditAmount, double dolar) {
        this.sortCode = generateSortCode;
        this.accountNumber = generateAccountNumber;
        this.currentBalance = currentBalance;
        this.bankName = bankName;
        this.ownerName = ownerName;
        this.creditAmount = creditAmount;
        this.dolar = dolar;
    }
    public Account(long id, String sortCode, String accountNumber, double currentBalance, String bankName, String ownerName, double creditAmount, double dolar) {
        this.id = id;
        this.sortCode = sortCode;
        this.accountNumber = accountNumber;
        this.currentBalance = currentBalance;
        this.bankName = bankName;
        this.ownerName = ownerName;
        this.creditAmount = creditAmount;
        this.dolar = dolar;
    }

    public Account(long id, String sortCode, String accountNumber, double currentBalance, String bankName, String ownerName, List<Transaction> transactions, double creditAmount, double dolar) {
        this.id = id;
        this.sortCode = sortCode;
        this.accountNumber = accountNumber;
        this.currentBalance = currentBalance;
        this.bankName = bankName;
        this.ownerName = ownerName;
        this.transactions = transactions;
        this.creditAmount = creditAmount;
        this.dolar = dolar;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getSortCode() {
        return sortCode;
    }
    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public double getCurrentBalance() {
        return currentBalance;
    }
    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }
    public String getOwnerName() {
        return ownerName;
    }
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public List<Transaction> getTransactions() {
        return transactions;
    }
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
    public double getCreditAmount(){
        return creditAmount;
    }
    public void setCreditAmount(double creditAmount){
        this.creditAmount = creditAmount;
    }
    public double getDolar(){
        return dolar;
    }
    public void setDolar(double dolar){
        this.dolar = dolar;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", sortCode='" + sortCode + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", currentBalance=" + currentBalance +
                ", bankName='" + bankName + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", credit='" + creditAmount + '\'' +
                ", dolar='" + dolar + '\'' +
                '}';
    }
}
