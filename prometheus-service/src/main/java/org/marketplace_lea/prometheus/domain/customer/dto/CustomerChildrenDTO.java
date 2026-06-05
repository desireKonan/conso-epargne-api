package org.marketplace_lea.prometheus.domain.customer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * DTO représentant les filleuls du client connecté, organisés par niveau.
 *
 * <p>
 * Contient le nombre total de filleuls et un regroupement
 * par niveau (level1, level2, etc.).
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerChildrenDTO {
    private int totalChildrenCount;

    @Builder.Default
    private Map<String, List<ChildV2DTO>> childrenByLevel = new TreeMap<>();
}
