package org.marketplace_lea.prometheus.domain.template_messages.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.marketplace_lea.common.entities.transaction.TransactionNotificationTemplateEntity;
import org.marketplace_lea.common.repositories.TransactionNotificationTemplateJpaRepository;
import org.marketplace_lea.prometheus.domain.template_messages.dto.TransactionNotificationTemplateDto;
import org.marketplace_lea.prometheus.domain.template_messages.form.NotificationTemplateSearchCriteria;
import org.marketplace_lea.prometheus.domain.template_messages.form.TransactionNotificationTemplateForm;
import org.marketplace_lea.prometheus.domain.template_messages.mapper.TransactionNotificationTemplateMapper;
import org.marketplace_lea.prometheus.domain.template_messages.services.TransactionNotificationTemplateService;
import org.marketplace_lea.prometheus.domain.template_messages.services.specifications.TransactionNotificationTemplateSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionNotificationTemplateServiceImpl implements TransactionNotificationTemplateService {
    private final TransactionNotificationTemplateJpaRepository repository;
    private final TransactionNotificationTemplateMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionNotificationTemplateDto> search(NotificationTemplateSearchCriteria criteria, Pageable pageable) {
        Specification<TransactionNotificationTemplateEntity> spec = TransactionNotificationTemplateSpecifications.byCriteria(criteria);
        return repository.findAll(spec, pageable)
                .map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionNotificationTemplateDto findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Template introuvable avec l'ID : " + id));
    }

    @Override
    @Transactional
    public TransactionNotificationTemplateDto create(TransactionNotificationTemplateForm form) {
        // Vérification d'unicité de la clé (exacte)
        if (repository.getByKey(form.getKey()).isPresent()) {
            log.error("Transaction already exists !");
            throw new IllegalArgumentException("La clé '" + form.getKey() + "' est déjà utilisée.");
        }
        TransactionNotificationTemplateEntity entity = mapper.toEntity(form);
        var templateSaved = repository.save(entity);
        log.info("Transaction created: {}", templateSaved.getId());
        return mapper.toDto(templateSaved);
    }

    @Override
    @Transactional
    public TransactionNotificationTemplateDto update(Long id, TransactionNotificationTemplateForm form) {
        TransactionNotificationTemplateEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Template introuvable avec l'ID : " + id));

        // Vérification de l'unicité de la clé si elle a changé
        if (!existing.getKey().equals(form.getKey())) {
            if (repository.getByKey(form.getKey()).isPresent()) {
                log.error("Transaction already exists !");
                throw new IllegalArgumentException("La clé '" + form.getKey() + "' est déjà utilisée.");
            }
        }
        mapper.updateEntity(existing, form);
        var templateSaved = repository.save(existing);
        log.info("Transaction created: {}", templateSaved.getId());
        return mapper.toDto(templateSaved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Template introuvable avec l'ID : " + id);
        }
        repository.deleteById(id);
    }
}