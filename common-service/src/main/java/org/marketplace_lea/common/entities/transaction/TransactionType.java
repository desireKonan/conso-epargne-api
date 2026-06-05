package org.marketplace_lea.common.entities.transaction;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum TransactionType {
    COLLECT_DONATION("DON COLLECTE"),
    COLLECT_PARTICIPATION("PARTICIPATION COLLECTE"),
    INVESTMENT_BONUS("BONUS INVESTISSEMENT"),
    INVESTMENT_PAYMENT("ACHAT INVESTISSEMENT"),
    INVESTMENT_PROFIT_PAYMENT("PAIEMENT RSI"),
    ONLINE_COLLECT_DONATION("DON COLLECTE EN LIGNE"),
    ORDER_PAYMENT("PAIEMENT COMMANDE"),
    SPECIAL_BONUS("BONUS SPECIAL"),
    PARTNER_BONUS("BONUS PARTENAIRE"),
    NETWORK_PAYMENT("PAIEMENT RESEAU"),
    MEMBERSHIP_PAYMENT("PAIEMENT ABONNEMENT"),
    WITHDRAWAL("RETRAIT ESPECES"),
    LEA_COIN_PAYMENT("ACHAT JETONS LEA"),
    POINT_TO_LEA_COIN_TRANSFERT("ECHANGE POINTS -> LEA"),
    LEA_COIN_TO_POINT_TRANSFERT("ECHANGE LEA -> POINTS"),
    PROFIT_TO_LEA_COIN_TRANSFERT("ECHANGE RSI -> LEA"),
    PERSONAL_SAVING("EPARGNE"),
    CONSOM_TRANSFER("TRANSFERT DE CONSOM"),
    CONSOM_ACHAT("ACHAT DE CONSOM"),
    CONSOM_VENTE("VENTE DE CONSOM"),
    VOUCHER_PAYMENT("BON D'ACHAT"),
    LEA_AFFILIATION_PAYMENT("PAIEMENT AFFILIATION LEA"),
    COMMISSION("COMMISSION");

    private final String label;

    TransactionType(String label) {
        this.label = label;
    }

    private static final Map<String, TransactionType> BY_LABEL = new HashMap<>();

    static {
        for (TransactionType e: values()) {
            BY_LABEL.put(e.label, e);
        }
    }

    public static TransactionType fromLabel(String label) {
        return BY_LABEL.get(label);
    }
}
