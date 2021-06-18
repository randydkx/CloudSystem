package cn.edu.njust.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class Bank implements Serializable {
    private String accountID;
    private BigDecimal amount;
    private String password;

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
