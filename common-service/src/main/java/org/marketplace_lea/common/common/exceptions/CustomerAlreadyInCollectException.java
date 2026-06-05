package org.marketplace_lea.common.common.exceptions;

public class CustomerAlreadyInCollectException extends ApplicationException {
    public CustomerAlreadyInCollectException(){
        super("Vous participez déja à une collecte !");
    }
}
