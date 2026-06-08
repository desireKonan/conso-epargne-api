package org.marketplace_lea.order.domain.inventory.dto;

public record EnsignV2SearchForm(
    String searchKeyword,
    Boolean available,
    Boolean isDeleted
) {}
