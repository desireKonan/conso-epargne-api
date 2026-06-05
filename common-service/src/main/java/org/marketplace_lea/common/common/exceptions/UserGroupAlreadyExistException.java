package org.marketplace_lea.common.common.exceptions;

public class UserGroupAlreadyExistException extends ApplicationException {
    public UserGroupAlreadyExistException() {
        super("Ce groupe utilisateur existe déja !");
    }

}
