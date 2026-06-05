package org.marketplace_lea.common.client.yellika.sms.provider;

import org.marketplace_lea.common.client.yellika.sms.form.YellikaSmsForm;

public interface SmsProvider {
    void sendSms(YellikaSmsForm form);
}
