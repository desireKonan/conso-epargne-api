package org.marketplace_lea.common.services.scheduler.services;

import org.marketplace_lea.common.entities.transaction.TransactionV2Entity;

/**
 * Service pour gérer les tâches planifiées liées aux paiements
 */
public interface PaymentSchedulerService {
    /**
     * Planifie la vérification du statut d'une transaction Dilaac
     */
    void scheduleDilaacTransactionStatusCheck(TransactionV2Entity transaction);
    
    /**
     * Arrête la vérification du statut d'une transaction Dilaac
     */
    void stopDilaacTransactionStatusCheck(String transactionId);
    
    /**
     * Vérifie si une tâche de vérification est active pour une transaction
     */
    boolean isDilaacStatusCheckActive(String transactionId);
    
    /**
     * Planifie une tâche de vérification avec des paramètres personnalisés
     */
    void scheduleStatusCheck(String transactionId, String transactionType, int repeatCount, int intervalSeconds);
    
    /**
     * Planifie une tâche de nettoyage des transactions expirées
     */
    void scheduleExpiredTransactionsCleanup();
}