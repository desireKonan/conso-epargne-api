package org.marketplace_lea.prometheus.domain.payment_method.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.common.common.service.storage.StorageService;
import org.marketplace_lea.prometheus.domain.payment_method.dto.PaymentMethodDTO;
import org.marketplace_lea.prometheus.domain.payment_method.entities.PaymentMethodEntity;
import org.marketplace_lea.prometheus.domain.payment_method.form.PaymentMethodCreateForm;
import org.marketplace_lea.prometheus.domain.payment_method.form.PaymentMethodSearchCriteria;
import org.marketplace_lea.prometheus.domain.payment_method.form.PaymentMethodUpdateForm;
import org.marketplace_lea.prometheus.domain.payment_method.mapper.PaymentMethodMapper;
import org.marketplace_lea.prometheus.domain.payment_method.repository.PaymentMethodJpaRepository;
import org.marketplace_lea.prometheus.domain.payment_method.services.PaymentMethodService;
import org.marketplace_lea.prometheus.domain.payment_method.specifications.PaymentMethodSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

import static org.marketplace_lea.common.common.utils.FileStorageUtils.createNewFileName;
import static org.marketplace_lea.common.common.utils.MediaUtils.getPaymentMethodImageKey;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {
    private final PaymentMethodJpaRepository repository;
    private final PaymentMethodMapper mapper;
    private final StorageService storageService;

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentMethodDTO> search(PaymentMethodSearchCriteria criteria, Pageable pageable) {
        Specification<PaymentMethodEntity> spec = PaymentMethodSpecifications.byCriteria(criteria);
        return repository.findAll(spec, pageable)
                .map(this::toDtoWithImageUrl);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentMethodDTO findById(Long id) {
        return repository.findById(id)
                .map(this::toDtoWithImageUrl)
                .orElseThrow(() -> new EntityNotFoundException("Méthode de paiement non trouvée avec l'ID : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentMethodDTO findByProvider(String provider) {
        return repository.findByProvider(provider)
                .map(this::toDtoWithImageUrl)
                .orElseThrow(() -> new EntityNotFoundException("Méthode de paiement non trouvée avec le provider : " + provider));
    }

    @Override
    @Transactional
    public PaymentMethodDTO create(PaymentMethodCreateForm form) {
        if (repository.existsByProvider(form.getProvider())) {
            throw new IllegalArgumentException("Un provider '" + form.getProvider() + "' existe déjà.");
        }

        PaymentMethodEntity entity = mapper.toEntity(form);

        // Gérer l'upload de l'image
        MultipartFile image = form.getImage();
        if (image != null && !image.isEmpty()) {
            String fileName = createNewFileName(image.getOriginalFilename());
            entity.setImage(fileName);
            entity = repository.save(entity);
            storageService.store(image, getPaymentMethodImageKey(fileName));
        } else {
            entity = repository.save(entity);
        }

        return toDtoWithImageUrl(entity);
    }

    @Override
    @Transactional
    public PaymentMethodDTO update(Long id, PaymentMethodUpdateForm form) {
        PaymentMethodEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Méthode de paiement non trouvée avec l'ID : " + id));

        // Vérifier l'unicité du provider si modifié
        if (!existing.getProvider().equals(form.getProvider()) && repository.existsByProvider(form.getProvider())) {
            throw new IllegalArgumentException("Le provider '" + form.getProvider() + "' est déjà utilisé.");
        }

        mapper.updateEntity(existing, form);

        // Gérer l'upload de la nouvelle image
        MultipartFile image = form.getImage();
        if (image != null && !image.isEmpty()) {
            String fileName = createNewFileName(image.getOriginalFilename());
            existing.setImage(fileName);
            existing = repository.save(existing);
            storageService.store(image, getPaymentMethodImageKey(fileName));
        } else {
            existing = repository.save(existing);
        }

        return toDtoWithImageUrl(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Méthode de paiement non trouvée avec l'ID : " + id);
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public PaymentMethodDTO changeAvailability(Long id, boolean available) {
        PaymentMethodEntity entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Méthode de paiement non trouvée avec l'ID : " + id));
        entity.setAvailable(available);
        return toDtoWithImageUrl(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentMethodDTO> findAllMobilePaymentMethods() {
        return repository.findAllByAvailableAndOnline(true, true)
                .stream()
                .map(this::toDtoWithImageUrl)
                .toList();
    }

    // Méthode utilitaire pour générer l'URL de l'image dans le DTO
    private PaymentMethodDTO toDtoWithImageUrl(PaymentMethodEntity entity) {
        return mapper.toDto(entity);
    }
}