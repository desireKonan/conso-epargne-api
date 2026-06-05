package org.marketplace_lea.common.common.exceptions;

public class UsernameAlreadyExistException extends ApplicationException {
    public UsernameAlreadyExistException() {
        super("Ce nom d'utilisateur est déja utilisé");
    }
}
