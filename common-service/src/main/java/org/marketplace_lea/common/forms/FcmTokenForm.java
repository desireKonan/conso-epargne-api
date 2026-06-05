package org.marketplace_lea.common.forms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FcmTokenForm(@NotBlank(message = "Le token ne doit pas être vide") String token) {}
