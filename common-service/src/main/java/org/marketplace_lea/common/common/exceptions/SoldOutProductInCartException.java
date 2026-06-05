package org.marketplace_lea.common.common.exceptions;

public class SoldOutProductInCartException extends ApplicationException {
    public SoldOutProductInCartException(String message) {
        super(message);
    }

    public SoldOutProductInCartException() {
        super("Le panier contient des produits en rupture de stock !");
    }

}
