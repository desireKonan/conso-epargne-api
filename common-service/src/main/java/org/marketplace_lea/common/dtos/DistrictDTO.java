package org.marketplace_lea.common.dtos;

import lombok.Data;
import java.io.Serializable;

/**
 * A DTO for the {@link District} entity
 */
@Data
public class DistrictDTO implements Serializable {
    private String id;
    private String label;
    private float fees;
}