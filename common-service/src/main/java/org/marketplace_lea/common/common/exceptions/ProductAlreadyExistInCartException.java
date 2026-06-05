package org.marketplace_lea.common.common.exceptions;

public class ProductAlreadyExistInCartException extends ApplicationException {
    public ProductAlreadyExistInCartException() {
        super("Le produit a déja été ajouté au panier");
    }
}
