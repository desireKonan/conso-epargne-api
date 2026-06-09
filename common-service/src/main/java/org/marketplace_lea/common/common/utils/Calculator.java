package org.marketplace_lea.common.common.utils;

public final class Calculator {

    public static float calculateAmountWithFees(double amount, float fees) {
        float deliveryFees = deliveryFees(fees);
        return arroundAmount(amount * deliveryFees);
    }


    public static float deliveryFees(float fees) {
        return (float) (fees / 100.0);
    }

    public static float arroundAmount(double amount) {
        return (float) (Math.round(amount * 100.0) / 100.0);
    }
}
