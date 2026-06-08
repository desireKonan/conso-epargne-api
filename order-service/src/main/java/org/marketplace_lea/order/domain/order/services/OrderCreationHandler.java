package org.marketplace_lea.order.domain.order.services;

import org.marketplace_lea.order.domain.order.dto.CreateOrderV2Form;
import org.marketplace_lea.order.domain.order.dto.OrderV2DTO;

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
public interface OrderCreationHandler {
    
    /**
     * Handles the complete order creation process with all business logic.
     * 
     * @param createDTO The order creation form
     * @return The created order DTO
     */
    OrderV2DTO handleOrderCreation(CreateOrderV2Form createDTO);
}
