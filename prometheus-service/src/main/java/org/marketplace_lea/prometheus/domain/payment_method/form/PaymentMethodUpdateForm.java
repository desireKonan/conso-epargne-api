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
public class PaymentMethodUpdateForm {

    @NotBlank(message = "Le label est obligatoire")
    private String label;

    private Boolean available;

    private Boolean online;

    @NotBlank(message = "Le provider est obligatoire")
    private String provider;

    private MultipartFile image;

    @PositiveOrZero(message = "Les frais doivent être >= 0")
    private Float fees;
}