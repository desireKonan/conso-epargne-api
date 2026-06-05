package org.marketplace_lea.common.client.yellika.sms.form;

public record YellikaSmsForm(
        String sender_id,
        String message,
        String recipient
) {
}
