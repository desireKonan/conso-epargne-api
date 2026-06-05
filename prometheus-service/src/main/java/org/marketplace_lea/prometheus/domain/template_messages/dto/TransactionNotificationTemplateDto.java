package org.marketplace_lea.prometheus.domain.template_messages.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.marketplace_lea.common.entities.transaction.TransactionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionNotificationTemplateDto {
    private Long id;

    @NotBlank(message = "La clé est obligatoire")
    private String key;

    @NotNull(message = "Le type de transaction est obligatoire")
    private TransactionType transactionType;

    @NotNull(message = "Les titres sont obligatoires")
    private Map<String, String> titles;  // ex: {"fr": "Titre fr", "en": "Title en"}

    @NotNull(message = "Les messages sont obligatoires")
    private Map<String, String> messages; // ex: {"fr": "Message fr", "en": "Message en"}

    private boolean active = true;
}