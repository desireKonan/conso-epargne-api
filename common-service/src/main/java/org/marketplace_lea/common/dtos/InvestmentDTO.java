package org.marketplace_lea.common.dtos;

import lombok.Data;

@Data
public class InvestmentDTO {
    private String id;

    private String label;

    private Float counterparty;

    private float amount;

    private String currency;

    private float balance;

    private float minCoinAmountToBuy;

    private float availableCoinAmount;

    private String description;

    private boolean available;

    private String imageUrl;

    private String videoUrl;
}
