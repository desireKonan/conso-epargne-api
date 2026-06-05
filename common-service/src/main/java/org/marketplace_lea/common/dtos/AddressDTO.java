package org.marketplace_lea.common.dtos;

import lombok.Data;

@Data
public class AddressDTO {
    private Long id;

    private String label;

    private boolean def;

    private boolean status;

    private DistrictDTO district;

    private String latitude;

    private String longitude;
}
