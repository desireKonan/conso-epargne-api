package org.marketplace_lea.order.domain.order.handlers;

public interface CommissionHandler {
    void addPartnerCommission(String parentId, String phoneNumber, float amount);

    void addCommission(String accountId, float amount);
}
