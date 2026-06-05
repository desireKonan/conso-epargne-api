package org.marketplace_lea.common.common.exceptions;

public class LoginAlreadyExistException extends ApplicationException {
    public LoginAlreadyExistException (){
        super("Le numéro de télephone est déja utilisé par un autre compte ");
    }
}
