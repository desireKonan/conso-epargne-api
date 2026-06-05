package org.marketplace_lea.common.dtos;

public record SendConsomRequestPointsForm(
        String senderAccountId,
        String receiverAccountId,
        float amount,
        boolean removeReceiverBalance
) {
}
