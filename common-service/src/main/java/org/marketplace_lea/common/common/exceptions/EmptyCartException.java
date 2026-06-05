package org.marketplace_lea.common.common.exceptions;

public class EmptyCartException extends ApplicationException {
    public EmptyCartException() {
        super("Le panier est vide, veuillez ajouter des produits !");
    }
}
