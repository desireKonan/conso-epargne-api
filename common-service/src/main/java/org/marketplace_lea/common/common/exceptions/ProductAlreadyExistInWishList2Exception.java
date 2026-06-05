package org.marketplace_lea.common.common.exceptions;

import org.springframework.http.HttpStatus;

public class ProductAlreadyExistInWishList2Exception extends ConsoEpargneException {
    public ProductAlreadyExistInWishList2Exception() {
        super("Ce produit à déja été ajouté au marché express !", HttpStatus.CONFLICT);
    }
}
