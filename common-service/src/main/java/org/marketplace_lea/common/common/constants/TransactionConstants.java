package org.marketplace_lea.common.common.constants;

import org.marketplace_lea.common.entities.transaction.TransactionType;

import static org.marketplace_lea.common.entities.transaction.TransactionType.LEA_COIN_PAYMENT;
import static org.marketplace_lea.common.entities.transaction.TransactionType.ONLINE_COLLECT_DONATION;
import static org.marketplace_lea.common.entities.transaction.TransactionType.ORDER_PAYMENT;

public interface TransactionConstants {
    TransactionType[] ONLINE_TRANSACTION_TYPES = {
            ORDER_PAYMENT,
            ONLINE_COLLECT_DONATION,
            LEA_COIN_PAYMENT
    };

    String VALIDATED_KEY = "01";
    String PENDING_KEY = "02";
    String FAILED_KEY = "03";
    String ALL_KEY = "00";

    String PERCENT_KEY = "%";
    String AMOUNT_KEY = "M";

    String VALIDATED_PERCENT_KEY = VALIDATED_KEY.concat(PERCENT_KEY);
    String PENDING_PERCENT_KEY = PENDING_KEY.concat(PERCENT_KEY);
    String FAILED_PERCENT_KEY = FAILED_KEY.concat(PERCENT_KEY);
}
