package org.marketplace_lea.order.domain.inventory.services;

import org.marketplace_lea.order.common.entities.inventory.ShelfV2Entity;
import org.marketplace_lea.order.domain.inventory.dto.CreateShelfV2Form;
import org.marketplace_lea.order.domain.inventory.dto.ShelfV2DTO;
import org.marketplace_lea.order.domain.inventory.dto.UpdateShelfV2Form;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ShelfV2Service {
    
    ShelfV2DTO createShelf(CreateShelfV2Form form, MultipartFile image);
    
    ShelfV2DTO getById(String id);
    
    Optional<ShelfV2DTO> findById(String id);
    
    Page<ShelfV2DTO> searchShelves(String ensignId, Pageable pageable);
    
    List<ShelfV2DTO> findShelvesByEnsignId(String ensignId);
    
    ShelfV2DTO updateShelf(String id, UpdateShelfV2Form form, MultipartFile image);
    
    void updateRank(String currentShelfId, String referralShelfId);
    
    void updateRank(String currentShelfId, int position);
    
    void performAction(String shelfId, String action, String referralShelfId);
    
    void deleteById(String id);
    
    void delete(ShelfV2Entity shelf);
}
