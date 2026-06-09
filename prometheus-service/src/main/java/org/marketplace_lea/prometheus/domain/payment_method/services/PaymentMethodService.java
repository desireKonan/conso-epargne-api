package org.marketplace_lea.prometheus.domain.payment_method.services;

import org.marketplace_lea.prometheus.domain.payment_method.dto.PaymentMethodDTO;
import org.marketplace_lea.prometheus.domain.payment_method.form.PaymentMethodCreateForm;
import org.marketplace_lea.prometheus.domain.payment_method.form.PaymentMethodSearchCriteria;
import org.marketplace_lea.prometheus.domain.payment_method.form.PaymentMethodUpdateForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentMethodService {
    Page<PaymentMethodDTO> search(PaymentMethodSearchCriteria criteria, Pageable pageable);

    PaymentMethodDTO findById(Long id);

    PaymentMethodDTO findByProvider(String provider);

    PaymentMethodDTO create(PaymentMethodCreateForm form);

    PaymentMethodDTO update(Long id, PaymentMethodUpdateForm form);

    void delete(Long id);

    PaymentMethodDTO changeAvailability(Long id, boolean available);

    List<PaymentMethodDTO> findAllMobilePaymentMethods();
}