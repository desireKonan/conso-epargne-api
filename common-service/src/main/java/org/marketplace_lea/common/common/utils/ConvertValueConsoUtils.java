package org.marketplace_lea.common.common.utils;

import org.marketplace_lea.common.common.constants.ConsoEpargneConstants;

import java.math.BigDecimal;

public final class ConvertValueConsoUtils {
    public static BigDecimal convertPointToLea(int points) {
        // 25 Pts = 0.05 Lea → 1 Pts = 0.002 Lea
        return BigDecimal.valueOf(points * ConsoEpargneConstants.POINT_MULTIPLIER);
    }

    public static int convertPointToConsom(int points) {
        // 25 Pts = 1 ConsoM
        return points / ConsoEpargneConstants.CONSOM_TO_POINTS;
    }

    public static BigDecimal convertPointToVoucher(float amount) {
        return BigDecimal.valueOf(amount * 0.0032);
    }
}
