package org.marketplace_lea.common.common.exceptions;

import org.marketplace_lea.common.client.no_wallet.exception.NoWalletException;
import org.marketplace_lea.common.client.paystack.exception.PaystackException;
import org.marketplace_lea.common.client.wane_delivery.exception.WaneDeliveryException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

/**
 * Gestion des exceptions avec spring MVC.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Encodage par défaut des messages json
     */
    public static final Charset ENCODAGE_MESSAGE = StandardCharsets.UTF_8;

    /**
     * Permet de spécifier que la réponse http renvoie du JSON.
     *
     * @param erreur   l'erreur à renvoyer.
     * @param response la réponse HTTP.
     */
    public static void setReponseJson(Object erreur, HttpServletResponse response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(erreur);

            response.setContentType("application/json");
            response.setCharacterEncoding(ENCODAGE_MESSAGE.name());
            OutputStream os = response.getOutputStream();
            os.write(json.getBytes(ENCODAGE_MESSAGE));
        } catch (IOException ex) {
            // Ne devrait pas arriver mais...
            log.error("Error while processing: {}", ex.getMessage(), ex);
        }
    }

    /**
     * Permet de gérer les exceptions lancées lorsqu'on n'a aucun résultat pour une requête en base.
     *
     * @param ex       l'exception lorsqu'on n'a aucun résultat pour une requête en base.
     * @param response la réponse HTTP.
     */
    @ExceptionHandler(NoResultException.class)
    public void handleNoResultException(NoResultException ex, HttpServletResponse response) {
        response.setStatus(NOT_FOUND.value());

        ApplicationError msg = new ApplicationError(
                NOT_FOUND.name(),
                NOT_FOUND.name(),
                NOT_FOUND.value()
        );

        log.warn(msg.customMessageCode());
        log.warn(ex.getMessage(), ex);
        setReponseJson(msg, response);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public void dataNotFoundException(DataNotFoundException ex, HttpServletResponse response) {
        response.setStatus(NOT_FOUND.value());

        ApplicationError msg = new ApplicationError(
                NOT_FOUND.name(),
                ex.getMessage(),
                NOT_FOUND.value(),
                ExceptionUserDisplay.WARNING
        );
        log.debug(ex.getMessage(), ex);
        setReponseJson(msg, response);
    }

    /**
     * Permet de gérer les exceptions liés à un échec d'authentification avec Spring Security.
     * C'est l'exception renvoyée lorsqu'une erreur est renvoyée liée à
     * l'annotation @Secured.
     *
     * @param ex       L'exception.
     * @param response La réponse HTTP.
     */
    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public void handleAuthenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException ex, HttpServletResponse response) {
        response.setStatus(UNAUTHORIZED.value());
        ApplicationError erreur = new ApplicationError(
                UNAUTHORIZED.name(),
                "Bad credentials !",
                UNAUTHORIZED.value(),
                ExceptionUserDisplay.ERROR
        );
        handleUnauthorizedError(ex, erreur, response);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public void handleAccountNotFoundException(AccountNotFoundException ex, HttpServletResponse response) {
        response.setStatus(UNAUTHORIZED.value());
        ApplicationError erreur = new ApplicationError(
                UNAUTHORIZED.name(),
                ex.getMessage(),
                UNAUTHORIZED.value(),
                ExceptionUserDisplay.ERROR
        );
        handleUnauthorizedError(ex, erreur, response);
    }

    /**
     * Permet de gérer les exceptions liées à un échec d'authentification car le(s)
     * rôle(s) requis pour accéder à la ressource sont absents.
     * <p>
     * La différence avec une erreur de type {@link } est
     * que l'utilisateur est bien authentifié mais ne dispose pas des droits suffisants pour
     * accéder à la ressource.
     *
     * @param ex       L'exception.
     * @param response La réponse http.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(AccessDeniedException ex, HttpServletResponse response) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        ApplicationError erreur = new ApplicationError(
                HttpStatus.FORBIDDEN.name(),
                "Vous n'avez pas le droit à cette ressource !",
                HttpStatus.FORBIDDEN.value(),
                ExceptionUserDisplay.ERROR
        );

        log.warn(erreur.customMessageCode());
        log.debug(ex.getMessage(), ex);
        setReponseJson(erreur, response);
    }

    private void handleUnauthorizedError(Exception ex, ApplicationError erreur, HttpServletResponse response) {
        log.warn(erreur.customMessageCode());
        log.debug(ex.getMessage(), ex);
        setReponseJson(erreur, response);
    }

    /**
     * Permet de gérer les exceptions runtime.
     *
     * @param ex       l'exception runtime.
     * @param response la réponse HTTP.
     */
    @ExceptionHandler(RuntimeException.class)
    public void handleRuntimeException(RuntimeException ex, HttpServletResponse response) {
        response.setStatus(INTERNAL_SERVER_ERROR.value());
        ApplicationError msg = new ApplicationError(
                INTERNAL_SERVER_ERROR.name(),
                "Une erreur s'est produite lors du traitement de la requête",
                INTERNAL_SERVER_ERROR.value(),
                ExceptionUserDisplay.ERROR
        );
        log.error(msg.customMessageCode(), ex);
        setReponseJson(msg, response);
    }

    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApplicationError msg = new ApplicationError(
                status.name(),
                ex.getMessage(),
                status.value(),
                ExceptionUserDisplay.ERROR
        );
        return new ResponseEntity<>(msg, headers, status);
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return getObjectResponseEntity(ex, headers, status);
    }


    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return getObjectResponseEntity(ex, headers, status);
    }

    private ResponseEntity<Object> getObjectResponseEntity(BindException ex, HttpHeaders headers, HttpStatus status) {
        List<FieldError> erreurs = ex.getAllErrors().stream()
                .filter(objectError -> (objectError instanceof FieldError))
                .map(objectError -> (FieldError) objectError)
                .toList();
        Optional<FieldError> firstError = erreurs.stream()
                .findFirst();
        String message = firstError.isPresent() ? firstError.get().getDefaultMessage() : "Fail to fetch validation error message.";
        ApplicationError msg = new ApplicationError(
                "VALIDATION",
                message,
                BAD_REQUEST.value(),
                ExceptionUserDisplay.ERROR
        );

        log.debug(ex.getMessage(), ex);
        return new ResponseEntity<>(msg, headers, status);
    }


    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApplicationError msg = new ApplicationError(
                UNSUPPORTED_MEDIA_TYPE.name(),
                String.format("Content type '%s' non supporté.", ex.getContentType()),
                UNSUPPORTED_MEDIA_TYPE.value(),
                ExceptionUserDisplay.WARNING
        );
        log.warn(msg.customMessageCode());
        return new ResponseEntity<>(msg, headers, status);
    }

    @ExceptionHandler(ApplicationException.class)
    public void handleApplicationException(ApplicationException ex, HttpServletResponse response) {
        response.setStatus(BAD_REQUEST.value());
        ApplicationError appError = new ApplicationError(
                HttpStatus.BAD_REQUEST.name(),
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                ExceptionUserDisplay.ERROR
        );

        log.warn(appError.customMessageCode());
        log.debug(ex.getMessage(), ex);
        setReponseJson(appError, response);
    }

    @ExceptionHandler(FileTypeException.class)
    public void handleStorageFileNotFound(FileTypeException ex, HttpServletResponse response) {
        response.setStatus(UNSUPPORTED_MEDIA_TYPE.value());

        ApplicationError msg = new ApplicationError(
                UNSUPPORTED_MEDIA_TYPE.name(),
                ex.getMessage(),
                UNSUPPORTED_MEDIA_TYPE.value()
        );

        log.warn(msg.customMessageCode());
        log.warn(ex.getMessage(), ex);
        setReponseJson(msg, response);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public void handleStorageFileNotFound(StorageFileNotFoundException ex, HttpServletResponse response) {
        response.setStatus(NOT_FOUND.value());

        ApplicationError msg = new ApplicationError(
                NOT_FOUND.name(),
                ex.getMessage(),
                NOT_FOUND.value()
        );
        log.warn(msg.customMessageCode());
        log.warn(ex.getMessage(), ex);
        setReponseJson(msg, response);
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<?> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        ApplicationError msg = new ApplicationError(
                UNAUTHORIZED.name(),
                "Login ou mot de passe incorrect.",
                UNAUTHORIZED.value(),
                ExceptionUserDisplay.ERROR
        );
        return new ResponseEntity<>(msg, UNAUTHORIZED);
    }


    @ExceptionHandler(PaystackException.class)
    protected ResponseEntity<?> handlePaystackException(PaystackException ex) {
        ApplicationError error = new ApplicationError(
                ex.getStatus().name(),
                ex.getMessage(),
                ex.getStatus().value(),
                ExceptionUserDisplay.ERROR
        );
        return new ResponseEntity<>(error, ex.getStatus());
    }

    @ExceptionHandler(NoWalletException.class)
    protected ResponseEntity<?> handleNoWalletException(NoWalletException ex) {
        ApplicationError error = new ApplicationError(
                ex.getStatus().name(),
                ex.getMessage(),
                ex.getStatus().value(),
                ExceptionUserDisplay.ERROR
        );
        return new ResponseEntity<>(error, ex.getStatus());
    }

    @ExceptionHandler(ConsoEpargneException.class)
    protected ResponseEntity<?> handleConsoEpargneException(ConsoEpargneException ex) {
        ApplicationError error = new ApplicationError(
                ex.getStatus().name(),
                ex.getMessage(),
                ex.getStatus().value(),
                ExceptionUserDisplay.ERROR
        );
        return new ResponseEntity<>(error, ex.getStatus());
    }


    @ExceptionHandler(WaneDeliveryException.class)
    protected ResponseEntity<?> handleWaneDeliveryException(WaneDeliveryException ex) {
        ApplicationError error = new ApplicationError(
                ex.getStatus().name(),
                ex.getMessage(),
                ex.getStatus().value(),
                ExceptionUserDisplay.ERROR
        );
        return new ResponseEntity<>(error, ex.getStatus());
    }

    @ExceptionHandler(LockedException.class)
    protected ResponseEntity<?> handleLockedException(LockedException ex, WebRequest request) {
        ApplicationError msg = new ApplicationError(
                FORBIDDEN.name(),
                "Vous êtes partenaire ? Vous devez payer votre abonnement avant de continuer ! Veuillez patienter quelques instants, nous vous redirigeons vers le portail de paiment...",
                903,
                ExceptionUserDisplay.ERROR
        );
        return new ResponseEntity<>(msg, FORBIDDEN);
    }

    @ExceptionHandler(DisabledException.class)
    protected ResponseEntity<?> handleDisabledException(DisabledException ex, WebRequest request) {
        ApplicationError msg = new ApplicationError(
                UNAUTHORIZED.name(),
                "Ce compte à été désactivé suite à une demande de suppression de compte. Veuillez contacter les administrateurs pour plus d'informations.",
                401,
                ExceptionUserDisplay.ERROR
        );
        return new ResponseEntity<>(msg, UNAUTHORIZED);
    }
}