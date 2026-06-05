package org.marketplace_lea.common.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerV2DTO {
    private String id;
    private AccountV2DTO account;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> phoneNumbers = new ArrayList<>();
    private String address;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}