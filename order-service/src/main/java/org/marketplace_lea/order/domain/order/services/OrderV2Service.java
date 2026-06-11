package org.marketplace_lea.order.domain.order.services;

import org.marketplace_lea.order.domain.order.dto.OrderCreationDTO;
import org.marketplace_lea.order.domain.order.form.OrderV2SearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderV2Service {
    
    Page<OrderCreationDTO> getAll(OrderV2SearchForm filterCriteria, Pageable pageable);
    
    OrderCreationDTO getById(String id);
    
    Optional<OrderCreationDTO> findById(String id);
    
    void delete(String id);
    
    void changeOrderStatus(String id, String status);
    
    void validateOrder(String id);
    
    void cancelOrder(String id);
}
