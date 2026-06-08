package org.marketplace_lea.order.domain.inventory.services.impl;

import org.marketplace_lea.common.common.exceptions.ConsoEpargneNotFoundDataException;
import org.marketplace_lea.common.common.service.storage.StorageService;
import org.marketplace_lea.order.common.repository.inventory.EnsignV2JpaRepository;
import org.marketplace_lea.order.common.entities.inventory.EnsignV2Entity;
import org.marketplace_lea.order.domain.inventory.dto.CreateEnsignV2Form;
import org.marketplace_lea.order.domain.inventory.dto.EnsignV2DTO;
import org.marketplace_lea.order.domain.inventory.dto.EnsignV2SearchForm;
import org.marketplace_lea.order.domain.inventory.dto.UpdateEnsignV2Form;
import org.marketplace_lea.order.domain.inventory.mapper.EnsignV2Mapper;
import org.marketplace_lea.order.domain.inventory.services.EnsignV2Service;
import org.marketplace_lea.order.domain.inventory.services.specifications.EnsignV2Specification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.marketplace_lea.common.common.utils.FileStorageUtils.createNewFileName;
import static org.marketplace_lea.common.common.utils.MediaUtils.getEnsignImageKey;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultEnsignV2Service implements EnsignV2Service {
    private final EnsignV2JpaRepository ensignRepository;
    private final StorageService storageService;
    private final EnsignV2Mapper ensignV2Mapper;

    private EnsignV2Entity getEntityById(String id) {
        return ensignRepository.findById(id)
                .orElseThrow(() -> new ConsoEpargneNotFoundDataException("Enseigne introuvable: " + id));
    }

    @Override
    @Transactional
    public EnsignV2DTO createEnsign(CreateEnsignV2Form form, MultipartFile image) {
        log.info("[DefaultEnsignV2Service.createEnsign] Création d'une nouvelle enseigne: {}", form.label());

        EnsignV2Entity ensign = ensignV2Mapper.toEntity(form);
        ensign.setRank((int) ensignRepository.count() + 1);

        if (image != null && !image.isEmpty()) {
            uploadEnsignImage(ensign, image);
        } else {
            throw new IllegalArgumentException("L'image de l'enseigne est obligatoire");
        }

        var ensignSaved = ensignRepository.save(ensign);
        log.info("[DefaultEnsignV2Service.createEnsign] Création d'une nouvelle enseigne: {}", ensignSaved.getId());
        return ensignV2Mapper.toDTO(ensignSaved);
    }

    @Override
    public EnsignV2DTO getById(String id) {
        return ensignV2Mapper.toDTO(getEntityById(id));
    }

    @Override
    public Optional<EnsignV2DTO> findById(String id) {
        return ensignRepository.findById(id)
                .map(ensignV2Mapper::toDTO);
    }

    @Override
    public Page<EnsignV2DTO> findAll(EnsignV2SearchForm form, Pageable pageable) {
        var spec = EnsignV2Specification.buildSpecification(form);
        return ensignRepository.findAll(spec, pageable)
                .map(ensignV2Mapper::toDTO);
    }

    @Override
    @Transactional
    public EnsignV2DTO updateEnsign(String id, UpdateEnsignV2Form form, MultipartFile image) {
        log.info("[DefaultEnsignV2Service.updateEnsign] Mise à jour de l'enseigne: {}", id);
        EnsignV2Entity ensign = getEntityById(id);

        if (form != null) {
            ensignV2Mapper.mapToEntity(form, ensign);
        }

        if (image != null) {
            uploadEnsignImage(ensign, image);
        }

        var ensignSaved = ensignRepository.save(ensign);
        log.info("[DefaultEnsignV2Service.updateEnsign] Mise à jour de l'enseigne: {}", ensignSaved.getId());
        return ensignV2Mapper.toDTO(ensignSaved);
    }

    @Override
    @Transactional
    public void updateRank(String currentEnsignId, String referralEnsignId) {
        EnsignV2Entity current = getEntityById(currentEnsignId);
        var prevOrSupEnsignFound = ensignRepository.findById(referralEnsignId);

        prevOrSupEnsignFound.ifPresent(ensignV2Entity -> {
            int prevOrSupRank = ensignV2Entity.getRank();
            ensignV2Entity.setRank(current.getRank());
            current.setRank(prevOrSupRank);
            ensignRepository.saveAll(List.of(current, ensignV2Entity));
        });
    }

    @Override
    @Transactional
    public void updateRank(String currentEnsignId, int position) {
        EnsignV2Entity current = getEntityById(currentEnsignId);
        EnsignV2Entity toChange = ensignRepository.findByRank(position).orElse(null);
        if (toChange == null || toChange.getRank() == 0) {
            current.setRank(position);
            ensignRepository.save(current);
        } else {
            int toChangeRank = toChange.getRank();
            toChange.setRank(current.getRank());
            current.setRank(toChangeRank);
            ensignRepository.saveAll(List.of(current, toChange));
        }
    }

    @Override
    @Transactional
    public void performAction(String ensignId, String action, String referralEnsignId) {
        EnsignV2Entity ensign = getEntityById(ensignId);
        if (action.equals("activate")) {
            ensign.setAvailable(true);
            ensignRepository.save(ensign);
            return;
        }
        if (action.equals("deactivate")) {
            ensign.setAvailable(false);
            ensignRepository.save(ensign);
            return;
        }
        if (action.equals("rankUp") && referralEnsignId != null && !referralEnsignId.isEmpty()) {
            updateRank(ensignId, referralEnsignId);
            return;
        }
        if (action.equals("rankDown") && referralEnsignId != null && !referralEnsignId.isEmpty()) {
            updateRank(ensignId, referralEnsignId);
        }
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        EnsignV2Entity ensign = getEntityById(id);
        ensign.setDeletedAt(LocalDateTime.now());
        ensignRepository.save(ensign);
    }

    @Override
    @Transactional
    public void delete(EnsignV2DTO ensign) {
        log.info("Deleting ensign with id {}", ensign.getId());
        deleteById(ensign.getId());
    }

    private void uploadEnsignImage(EnsignV2Entity ensign, MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return;
        }
        String fileName = createNewFileName(image.getOriginalFilename());
        ensign.setImage(fileName);
        try {
            storageService.store(image, getEnsignImageKey(fileName));
        } catch (Exception e) {
            log.error("Erreur lors de la sauvegarde de l'image de l'enseigne", e);
            throw new RuntimeException("Impossible de sauvegarder l'image", e);
        }
    }
}
