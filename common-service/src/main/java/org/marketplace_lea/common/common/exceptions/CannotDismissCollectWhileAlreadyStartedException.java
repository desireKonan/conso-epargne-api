package org.marketplace_lea.common.common.exceptions;

public class CannotDismissCollectWhileAlreadyStartedException extends ApplicationException {
    public CannotDismissCollectWhileAlreadyStartedException() {
        super("Vous ne pouvez pas quitter une collecte qui a déja démarrée !");
    }
}
