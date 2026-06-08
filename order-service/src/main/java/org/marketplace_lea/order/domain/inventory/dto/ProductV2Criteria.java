package org.marketplace_lea.order.domain.inventory.dto;

import lombok.Data;

@Data
public class ProductV2Criteria {
    private String searchKeyword;
    private String ensignId;
    private Boolean soldOut;
    private Boolean isDeleted = false; // Par défaut, on ne renvoie que les produits non supprimés
}
