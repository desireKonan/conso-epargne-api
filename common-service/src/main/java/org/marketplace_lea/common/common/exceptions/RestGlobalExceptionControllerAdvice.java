package org.marketplace_lea.common.common.exceptions;

import jakarta.persistence.NoResultException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.common.client.no_wallet.exception.NoWalletException;
import org.marketplace_lea.common.client.paystack.exception.PaystackException;
import org.marketplace_lea.common.client.wane_delivery.exception.WaneDeliveryException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.security.auth.login.AccountNotFoundException;
import java.net.URI;
import java.time.Instant;
import java.util.stream.Collectors;

/**
 * Gestion centralisée des exceptions avec Problem Details (RFC 7807).
 * <p>
 * Activez spring.mvc.problemdetails.enabled=true dans votre configuration.
 * </p>
 */
@Slf4j
@RestControllerAdvice
public class RestGlobalExceptionControllerAdvice extends ResponseEntityExceptionHandler {
    // ==================== CONSTANTES ====================
    private static final String DEFAULT_ERROR_MESSAGE = "Une erreur s'est produite lors du traitement de la requête";
    private static final String VALIDATION_ERROR_TYPE = "https://api.consoepargne.com/errors/validation";
    private static final String BUSINESS_ERROR_TYPE = "https://api.consoepargne.com/errors/business";
    private static final String SECURITY_ERROR_TYPE = "https://api.consoepargne.com/errors/security";
    private static final String EXTERNAL_SERVICE_ERROR_TYPE = "https://api.consoepargne.com/errors/external-service";

    // ==================== EXCEPTIONS MÉTIER ====================
    @ExceptionHandler(NoResultException.class)
    public ProblemDetail handleNoResultException(NoResultException ex) {
        log.warn("Aucun résultat trouvé: {}", ex.getMessage());
        return createProblemDetail(
                HttpStatus.NOT_FOUND,
                "RESSOURCE_INTROUVABLE",
                ex.getMessage(),
                BUSINESS_ERROR_TYPE,
                ExceptionUserDisplay.WARNING
        );
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ProblemDetail handleDataNotFoundException(DataNotFoundException ex) {
        log.debug("Donnée non trouvée: {}", ex.getMessage());
        return createProblemDetail(
                HttpStatus.NOT_FOUND,
                "DATA_NOT_FOUND",
                ex.getMessage(),
                BUSINESS_ERROR_TYPE,
                ExceptionUserDisplay.WARNING
        );
    }

    @ExceptionHandler(ApplicationException.class)
    public ProblemDetail handleApplicationException(ApplicationException ex) {
        log.warn("Erreur applicative: {}", ex.getMessage());
        log.debug(ex.getMessage(), ex);
        return createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "BUSINESS_ERROR",
                ex.getMessage(),
                BUSINESS_ERROR_TYPE,
                ExceptionUserDisplay.ERROR
        );
    }

    @ExceptionHandler(AmountCannotBeNegativeException.class)
    public ProblemDetail handleAmountCannotBeNegativeException(AmountCannotBeNegativeException ex) {
        log.debug("Montant invalide: {}", ex.getMessage());
        return createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "INVALID_AMOUNT",
                ex.getMessage(),
                BUSINESS_ERROR_TYPE,
                ExceptionUserDisplay.ERROR
        );
    }

    @ExceptionHandler(InsufficientFundException.class)
    public ProblemDetail handleInsufficientFundException(InsufficientFundException ex) {
        log.debug("Fonds insuffisants: {}", ex.getMessage());
        return createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "INSUFFICIENT_FUNDS",
                ex.getMessage(),
                BUSINESS_ERROR_TYPE,
                ExceptionUserDisplay.ERROR
        );
    }

    @ExceptionHandler(EmptyCartException.class)
    public ProblemDetail handleEmptyCartException(EmptyCartException ex) {
        log.debug("Panier vide: {}", ex.getMessage());
        return createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "EMPTY_CART",
                ex.getMessage(),
                BUSINESS_ERROR_TYPE,
                ExceptionUserDisplay.ERROR
        );
    }

    @ExceptionHandler(SoldOutProductInCartException.class)
    public ProblemDetail handleSoldOutProductInCartException(SoldOutProductInCartException ex) {
        log.debug("Produit en rupture: {}", ex.getMessage());
        return createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "PRODUCT_OUT_OF_STOCK",
                ex.getMessage(),
                BUSINESS_ERROR_TYPE,
                ExceptionUserDisplay.ERROR
        );
    }

    // ==================== EXCEPTIONS DE SÉCURITÉ ====================

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ProblemDetail handleAuthenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException ex) {
        log.warn("Tentative d'accès sans authentification");
        return createProblemDetail(
                HttpStatus.UNAUTHORIZED,
                "AUTHENTICATION_REQUIRED",
                "Authentification requise pour accéder à cette ressource",
                SECURITY_ERROR_TYPE,
                ExceptionUserDisplay.ERROR
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException ex) {
        log.warn("Échec d'authentification: {}", ex.getMessage());
        return createProblemDetail(
                HttpStatus.UNAUTHORIZED,
                "INVALID_CREDENTIALS",
                "Login ou mot de passe incorrect",
                SECURITY_ERROR_TYPE,
                ExceptionUserDisplay.ERROR
        );
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ProblemDetail handleAccountNotFoundException(AccountNotFoundException ex) {
        log.warn("Compte non trouvé: {}", ex.getMessage());
        return createProblemDetail(
                HttpStatus.UNAUTHORIZED,
                "ACCOUNT_NOT_FOUND",
                ex.getMessage(),
                SECURITY_ERROR_TYPE,
                ExceptionUserDisplay.ERROR
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Accès refusé: {}", ex.getMessage());
        return createProblemDetail(
                HttpStatus.FORBIDDEN,
                "ACCESS_DENIED",
                "Vous n'avez pas le droit d'accéder à cette ressource",
                SECURITY_ERROR_TYPE,
                ExceptionUserDisplay.ERROR
        );
    }

    @ExceptionHandler(LockedException.class)
    public ProblemDetail handleLockedException(LockedException ex) {
        log.warn("Compte verrouillé: {}", ex.getMessage());
        ProblemDetail problemDetail = createProblemDetail(
                HttpStatus.FORBIDDEN,
                "ACCOUNT_LOCKED",
                "Compte partenaire verrouillé",
                SECURITY_ERROR_TYPE,
                ExceptionUserDisplay.ERROR
        );
        problemDetail.setProperty("redirectUrl", "/payment/subscription");
        return problemDetail;
    }

    @ExceptionHandler(DisabledException.class)
    public ProblemDetail handleDisabledException(DisabledException ex) {
        log.warn("Compte désactivé: {}", ex.getMessage());
        return createProblemDetail(
                HttpStatus.UNAUTHORIZED,
                "ACCOUNT_DISABLED",
                "Ce compte a été désactivé. Veuillez contacter les administrateurs.",
                SECURITY_ERROR_TYPE,
                ExceptionUserDisplay.ERROR
        );
    }

    // ==================== EXCEPTIONS CLIENTS EXTERNES ====================

    @ExceptionHandler(PaystackException.class)
    public ProblemDetail handlePaystackException(PaystackException ex) {
        log.error("Erreur Paystack: {}", ex.getMessage());
        return createProblemDetail(
                ex.getStatus(),
                "PAYSTACK_ERROR",
                ex.getMessage(),
                EXTERNAL_SERVICE_ERROR_TYPE,
                ExceptionUserDisplay.ERROR
        );
    }

    @ExceptionHandler(NoWalletException.class)
    public ProblemDetail handleNoWalletException(NoWalletException ex) {
        log.error("Portefeuille introuvable: {}", ex.getMessage());
        return createProblemDetail(
                ex.getStatus(),
                "WALLET_NOT_FOUND",
                ex.getMessage(),
                EXTERNAL_SERVICE_ERROR_TYPE,
                ExceptionUserDisplay.ERROR
        );
    }

    @ExceptionHandler(ConsoEpargneException.class)
    public ProblemDetail handleConsoEpargneException(ConsoEpargneException ex) {
        log.error("Erreur ConsoEpargne: {}", ex.getMessage());
        return createProblemDetail(
                ex.getStatus(),
                "CONSO_EPARGNE_ERROR",
                ex.getMessage(),
                EXTERNAL_SERVICE_ERROR_TYPE,
                ExceptionUserDisplay.ERROR
        );
    }

    @ExceptionHandler(WaneDeliveryException.class)
    public ProblemDetail handleWaneDeliveryException(WaneDeliveryException ex) {
        log.error("Erreur livraison Wane: {}", ex.getMessage());
        return createProblemDetail(
                ex.getStatus(),
                "DELIVERY_ERROR",
                ex.getMessage(),
                EXTERNAL_SERVICE_ERROR_TYPE,
                ExceptionUserDisplay.ERROR
        );
    }

    // ==================== EXCEPTIONS STOCKAGE ====================
    @ExceptionHandler(FileTypeException.class)
    public ProblemDetail handleFileTypeException(FileTypeException ex) {
        log.warn("Type de fichier non supporté: {}", ex.getMessage());
        return createProblemDetail(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "UNSUPPORTED_FILE_TYPE",
                ex.getMessage(),
                BUSINESS_ERROR_TYPE,
                ExceptionUserDisplay.WARNING
        );
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ProblemDetail handleStorageFileNotFoundException(StorageFileNotFoundException ex) {
        log.warn("Fichier non trouvé: {}", ex.getMessage());
        return createProblemDetail(
                HttpStatus.NOT_FOUND,
                "FILE_NOT_FOUND",
                ex.getMessage(),
                BUSINESS_ERROR_TYPE,
                ExceptionUserDisplay.WARNING
        );
    }

    // ==================== EXCEPTIONS DE VALIDATION ====================
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolationException(ConstraintViolationException ex) {
        String violations = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        log.debug("Violation de contrainte: {}", violations);

        ProblemDetail problemDetail = createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                violations,
                VALIDATION_ERROR_TYPE,
                ExceptionUserDisplay.ERROR
        );
        problemDetail.setProperty("violations",
                ex.getConstraintViolations().stream()
                        .map(violation -> java.util.Map.of(
                                "propertyPath", violation.getPropertyPath().toString(),
                                "message", violation.getMessage()
                        ))
                        .collect(Collectors.toList())
        );
        return problemDetail;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        String firstErrorMessage = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Erreur de validation des données");

        log.debug("Erreur de validation: {}", firstErrorMessage);

        ProblemDetail problemDetail = createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                firstErrorMessage,
                VALIDATION_ERROR_TYPE,
                ExceptionUserDisplay.ERROR
        );

        // Ajouter la liste complète des erreurs de validation
        problemDetail.setProperty("errors",
                ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(error -> java.util.Map.of(
                                "field", error.getField(),
                                "rejectedValue", String.valueOf(error.getRejectedValue()),
                                "message", error.getDefaultMessage()
                        ))
                        .collect(Collectors.toList())
        );

        return ResponseEntity.status(status).body(problemDetail);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            org.springframework.http.HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        log.debug("Paramètre requis manquant: {}", ex.getParameterName());
        var problemDetail = createProblemDetail(
                HttpStatus.BAD_REQUEST,
                "MISSING_PARAMETER",
                String.format("Paramètre requis manquant: %s", ex.getParameterName()),
                VALIDATION_ERROR_TYPE,
                ExceptionUserDisplay.ERROR
        );

        return ResponseEntity.status(status).body(problemDetail);
    }

    // ==================== EXCEPTIONS RUNTIME (Fallback) ====================

    @ExceptionHandler(RuntimeException.class)
    public ProblemDetail handleRuntimeException(RuntimeException ex) {
        log.error("Erreur inattendue: {}", ex.getMessage(), ex);
        return createProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_ERROR",
                DEFAULT_ERROR_MESSAGE,
                null,
                ExceptionUserDisplay.ERROR
        );
    }

    // ==================== MÉTHODES UTILITAIRES ====================

    private ProblemDetail buildProblemDetail(
            HttpStatus status,
            String errorCode,
            String message,
            String typeUri,
            ExceptionUserDisplay displayType
    ) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, message);
        problemDetail.setTitle(status.getReasonPhrase());
        problemDetail.setInstance(URI.create("/api/errors/" + errorCode.toLowerCase()));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("errorCode", errorCode);
        problemDetail.setProperty("displayType", displayType.name());

        if (typeUri != null) {
            problemDetail.setType(URI.create(typeUri));
        }

        return problemDetail;
    }

    private ProblemDetail createProblemDetail(
            HttpStatus status,
            String errorCode,
            String message,
            String typeUri,
            ExceptionUserDisplay displayType) {
        return buildProblemDetail(status, errorCode, message, typeUri, displayType);
    }
}