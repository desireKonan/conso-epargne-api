package org.marketplace_lea.common.dtos;

import lombok.Getter;

import java.util.List;

@Getter
public class AccountInvestmentWrapper {
    private final float totalInvestmentProfits;
    private final List<AccountInvestmentDTO> investments;

    public AccountInvestmentWrapper(List<AccountInvestmentDTO> investments) {
        this.investments = investments;
        this.totalInvestmentProfits = investments.stream()
                .map(AccountInvestmentDTO::getProfits)
                .reduce(0f, Float::sum);
    }
}
