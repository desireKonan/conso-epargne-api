package org.marketplace_lea.common.client.paystack.form;

import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;

@Data
public class CustomerForm {
    @Email
    @NotBlank
    private String email;
    
    private String first_name;
    private String last_name;
    private String phone;
    private Map<String, Object> metadata;
}