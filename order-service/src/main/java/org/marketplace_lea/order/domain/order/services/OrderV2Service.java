package org.marketplace_lea.order.domain.order.services;

import org.marketplace_lea.order.domain.order.dto.OrderV2DTO;
import org.marketplace_lea.order.domain.order.dto.OrderV2SearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderV2Service {
    
    Page<OrderV2DTO> getAll(OrderV2SearchForm filterCriteria, Pageable pageable);
    
    OrderV2DTO getById(String id);
    
    Optional<OrderV2DTO> findById(String id);
    
    void delete(String id);
    
    void changeOrderStatus(String id, String status);
    
    void validateOrder(String id);
    
    void cancelOrder(String id);
}
