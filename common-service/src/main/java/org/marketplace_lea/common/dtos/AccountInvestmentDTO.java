package org.marketplace_lea.common.dtos;

import lombok.Data;

@Data
public class AccountInvestmentDTO {
    private InvestmentDTO investment;
    private float leaCoinAmout;
    private float profits;
}
