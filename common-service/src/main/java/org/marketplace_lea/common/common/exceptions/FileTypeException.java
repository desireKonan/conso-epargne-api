package org.marketplace_lea.common.common.exceptions;

public class FileTypeException extends ApplicationException {
    public FileTypeException() {
        super("Format de fichier(s) invalide(s). Types acceptés: *.png, *.jpg, *jpeg, ...");
    }
}
