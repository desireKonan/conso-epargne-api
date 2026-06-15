package org.marketplace_lea.order.domain.order.handlers;

/**
 * Handler for order creation orchestration.
 * This service handles the complex business logic around order creation including:
 * - Cart validation
 * - Payment method handling
 * - Address management
 * - Payment detail creation
 * - Order history creation
 * - Event publishing
 * - Cart deletion
 */
public interface OrderHandler<C, R> {
    
    /**
     * Handles the complete order process (Creation, Validation) with all business logic.
     * 
     * @param command The order creation form
     * @return The created order DTO
     */
    R handle(C command);
}
