package org.marketplace_lea.prometheus.domain.template_messages.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.marketplace_lea.common.entities.transaction.TransactionType;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionNotificationTemplateForm {
    @NotBlank(message = "La clé est obligatoire")
    private String key;

    @NotNull(message = "Le type de transaction est obligatoire")
    private TransactionType transactionType;

    @NotNull(message = "Les titres sont obligatoires")
    private Map<String, String> titles = new HashMap<>();   // {"fr": "Titre", "en": "Title"}

    @NotNull(message = "Les messages sont obligatoires")
    private Map<String, String> messages = new HashMap<>(); // {"fr": "Message", "en": "Message"}

    private boolean active = true;
}