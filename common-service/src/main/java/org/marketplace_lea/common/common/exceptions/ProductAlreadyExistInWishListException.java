package org.marketplace_lea.common.common.exceptions;

public class ProductAlreadyExistInWishListException extends ApplicationException {
    public ProductAlreadyExistInWishListException() {
        super("Ce produit à déja été ajouté au marché express !");
    }
}
