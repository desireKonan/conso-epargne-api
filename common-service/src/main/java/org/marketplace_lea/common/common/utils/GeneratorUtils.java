package org.marketplace_lea.common.common.utils;

import org.marketplace_lea.common.common.constants.ConsoEpargneConstants;
import org.slf4j.MDC;

import java.util.Optional;
import java.util.UUID;

public final class GeneratorUtils {
    public static String generateRandomString(int taille) {
        UUID idOne = UUID.randomUUID();
        String str = idOne.toString();
        if (taille > 16) {
            return str;
        } else {
            return str.substring(0, taille);
        }
    }

    public static String generate5digitsCode() {
        int min = 10000;
        int max = 99999;
        return String.valueOf((int) (Math.random() * (max - min + 1) + min));
    }

    public static String generateProductId() {
        return String.format("PRD-%s", generate(7));
    }

    public static String generateShelfId() {
        return String.format("SHE-%s", generate(4));
    }

    public static String generateSubscriptionId() {
        return String.format("SUB-%s", generate(6));
    }

    public static String generateEnsignId() {
        return String.format("ENS-%s", generate(4));
    }

    public static String generateCollectId() {
        return String.format("CLT-%s", generate(5));
    }

    public static String generateWalletId() {
        return String.format("WLT-%s", generate(7));
    }

    private static String generate(int taille) {
        UUID idOne = UUID.randomUUID();
        String str = "%s" + idOne;
        int uid = str.hashCode();
        String filterStr = "" + uid;
        str = filterStr.replaceAll("-", "");
        return str.substring(0, taille);
    }

    public static String generateUsername(String firstname, String lastname) {
        return String.format("%s.%s%s", firstname.replaceAll("'", "")
                        .split(" ")[0], lastname.replaceAll("'", "")
                        .split(" ")[0], generate(4))
                .toLowerCase();
    }

    public static String generateOrderId() {
        return String.format("ORD-%s", generate(6));
    }

    public static String generateLocalityId() {
        return String.format("LOC-%s", generate(9));
    }

    public static String generateAffiliationCode() {
        return generateRandomString(7).toUpperCase();
    }

    public static String generateCustomerId() {
        return String.format("CUS-%s", generate(8));
    }

    public static String generateAccountId() {
        return String.format("ACC-%s", generate(8));
    }

    public static String generateAccountTypeId() {
        return String.format("ACC-TYPE-%s", generate(8));
    }

    public static String generatePaymentDetailsId() {
        return String.format("PD-%s", generate(8));
    }

    public static String generateVoucherId() {
        return String.format("VCH-%s", generate(5));
    }

    public static String generateInvestmentId() {
        return String.format("INV-%s", generate(5));
    }

    public static String generateTransactionId() {
        return "ce-" + UUID.randomUUID().toString();
    }

    public static String generateIPPId() {
        return "IPP-" + UUID.randomUUID().toString().substring(0, 18);
    }

    public static String generateDistrictId() {
        return String.format("DIS-%s", generate(6));
    }


    public static String generateTransactionReference() {
        return String.format("TXN_%s_%s", System.currentTimeMillis(), UUID.randomUUID().toString().substring(0, 8).toUpperCase());
    }

    public static String getCurrentCorrelationId() {
        return Optional.ofNullable(MDC.get(ConsoEpargneConstants.CORRELATION_ID_HEADER))
                .orElse(UUID.randomUUID().toString());
    }


    public static String bearerToken(String token) {
        return "Bearer " + token;
    }
}
