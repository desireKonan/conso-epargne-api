package org.marketplace_lea.order.domain.inventory.services;

import org.marketplace_lea.order.domain.inventory.dto.CreateEnsignV2Form;
import org.marketplace_lea.order.domain.inventory.dto.EnsignV2DTO;
import org.marketplace_lea.order.domain.inventory.dto.EnsignV2SearchForm;
import org.marketplace_lea.order.domain.inventory.dto.UpdateEnsignV2Form;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface EnsignV2Service {
    
    EnsignV2DTO createEnsign(CreateEnsignV2Form form, MultipartFile image);
    
    EnsignV2DTO getById(String id);
    
    Optional<EnsignV2DTO> findById(String id);
    
    Page<EnsignV2DTO> findAll(EnsignV2SearchForm form, Pageable pageable);
    
    EnsignV2DTO updateEnsign(String id, UpdateEnsignV2Form form, MultipartFile image);
    
    void updateRank(String currentEnsignId, String referralEnsignId);
    
    void updateRank(String currentEnsignId, int position);
    
    void performAction(String ensignId, String action, String referralEnsignId);
    
    void deleteById(String id);
    
    void delete(EnsignV2DTO ensign);
}
