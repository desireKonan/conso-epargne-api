package org.marketplace_lea.prometheus.domain.auth.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.marketplace_lea.common.dtos.CustomerTokenInfo;

import java.time.Instant;

/**
 * Réponse d'authentification V2.
 *
 * <p>Contient le token JWT d'accès, sa date d'expiration et
 * les informations publiques du client authentifié.</p>
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthTokenV2Response(

        /**
         * Token JWT d'accès à transmettre dans le header {@code Authorization: Bearer <token>}.
         */
        @JsonProperty("access_token")
        String accessToken,

        /**
         * Date et heure d'expiration du token (ISO-8601 UTC).
         */
        @JsonProperty("expire_at")
        Instant expireAt,

        /**
         * Informations publiques du client authentifié.
         */
        @JsonProperty("customer_infos")
        CustomerTokenInfo customer
) {
}
