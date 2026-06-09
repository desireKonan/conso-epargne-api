package org.marketplace_lea.prometheus.domain.payment_method.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodCreateForm {

    @NotBlank(message = "Le label est obligatoire")
    private String label;

    private boolean available = true;

    private boolean online = false;

    @NotBlank(message = "Le provider est obligatoire")
    private String provider;

    private MultipartFile image;

    @PositiveOrZero(message = "Les frais doivent être >= 0")
    private float fees = 0.0f;
}