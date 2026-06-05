package org.marketplace_lea.common.dtos;

import java.math.BigDecimal;

public class WalletAccountDTO {
    private String walletId;
    private BigDecimal investedCoinAmount;
    private BigDecimal balance;
    private String accountId;
    private String accountLogin;
    private String numberAndOperator;


    public WalletAccountDTO(String walletId, BigDecimal investedCoinAmount, BigDecimal balance, String accountId, String accountLogin, String numberAndOperator) {
        this.walletId = walletId;
        this.investedCoinAmount = investedCoinAmount;
        this.balance = balance;
        this.accountId = accountId;
        this.accountLogin = accountLogin;
        this.numberAndOperator = numberAndOperator;
    }

    // Getters
    public String getWalletId() {
        return walletId;
    }

    public BigDecimal getInvestedCoinAmount() {
        return investedCoinAmount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getAccountLogin() {
        return accountLogin;
    }

    public String getNumberAndOperator() {
        return numberAndOperator;
    }
}



