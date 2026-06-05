package org.marketplace_lea.common.common.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.text.MessageFormat;

/**
 * Objet utilisé pour transporter une erreur de l'application, spécifiés par son type, son code et son message.<br/>
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationError {
    private int code;
    private String type;
    private String message;
    private ExceptionUserDisplay display;

    public ApplicationError(String type, String message, int code, ExceptionUserDisplay display) {
        this.type = type;
        this.message = message;
        this.code = code;
        this.display = display;
    }

    public ApplicationError(String type, String message, int code) {
        this.type = type;
        this.message = message;
        this.code = code;
    }

    /**
     * Retourne le message préfixé par le code de l'erreur.
     *
     * @return Le message préfixé par le code de l'erreur.
     */
    @JsonIgnore
    public String customMessageCode() {
        return MessageFormat.format("[{0,number,0000}] {1}", getCode(), getMessage());
    }
}