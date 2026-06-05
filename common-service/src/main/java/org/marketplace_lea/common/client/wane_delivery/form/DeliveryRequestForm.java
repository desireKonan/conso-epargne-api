package org.marketplace_lea.common.client.wane_delivery.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryRequestForm {
    // Coordonnées de départ
    @JsonProperty("pickup_lat")
    @NotNull(message = "La latitude de départ est obligatoire")
    @DecimalMin(value = "-90.0", message = "La latitude doit être >= -90")
    @DecimalMax(value = "90.0", message = "La latitude doit être <= 90")
    private Double pickupLat;
    
    @JsonProperty("pickup_lng")
    @NotNull(message = "La longitude de départ est obligatoire")
    @DecimalMin(value = "-180.0", message = "La longitude doit être >= -180")
    @DecimalMax(value = "180.0", message = "La longitude doit être <= 180")
    private Double pickupLng;
    
    @JsonProperty("pickup_address")
    @NotBlank(message = "L'adresse de départ est obligatoire")
    @Size(max = 255, message = "L'adresse de départ ne peut pas dépasser 255 caractères")
    private String pickupAddress;
    
    // Coordonnées d'arrivée
    @JsonProperty("drop_lat")
    @NotNull(message = "La latitude d'arrivée est obligatoire")
    @DecimalMin(value = "-90.0", message = "La latitude doit être >= -90")
    @DecimalMax(value = "90.0", message = "La latitude doit être <= 90")
    private Double dropLat;
    
    @JsonProperty("drop_lng")
    @NotNull(message = "La longitude d'arrivée est obligatoire")
    @DecimalMin(value = "-180.0", message = "La longitude doit être >= -180")
    @DecimalMax(value = "180.0", message = "La longitude doit être <= 180")
    private Double dropLng;
    
    @JsonProperty("drop_address")
    @NotBlank(message = "L'adresse d'arrivée est obligatoire")
    @Size(max = 255, message = "L'adresse d'arrivée ne peut pas dépasser 255 caractères")
    private String dropAddress;
    
    // Type de véhicule
    @JsonProperty("vehicle_type")
    @NotBlank(message = "Le type de véhicule est obligatoire")
    private String vehicleType;
    
    // Informations client
    @JsonProperty("customer_name")
    @NotBlank(message = "Le nom du client est obligatoire")
    @Size(max = 100, message = "Le nom ne peut pas dépasser 100 caractères")
    private String customerName;
    
    @JsonProperty("customer_phone")
    @NotBlank(message = "Le téléphone du client est obligatoire")
    private String customerPhone;
    
    @JsonProperty("customer_email")
    @NotBlank(message = "L'email du client est obligatoire")
    @Email(message = "Format d'email invalide")
    @Size(max = 100, message = "L'email ne peut pas dépasser 100 caractères")
    private String customerEmail;
    
    // Informations sur les marchandises
    @JsonProperty("goods_type_id")
    @NotNull(message = "L'ID du type de marchandise est obligatoire")
    @Min(value = 1, message = "L'ID du type de marchandise doit être positif")
    private Integer goodsTypeId;
    
    @JsonProperty("goods_type_quantity")
    @NotBlank(message = "La quantité/description des marchandises est obligatoire")
    private String goodsTypeQuantity;
    
    // Mode de paiement
    @JsonProperty("payment_mode")
    @NotBlank(message = "Le mode de paiement est obligatoire")
    private String paymentMode;
    
    // Référence partenaire (conditionnelle selon paymentMode)
    @JsonProperty("partner_reference")
    @Size(max = 50, message = "La référence partenaire ne peut pas dépasser 50 caractères")
    private String partnerReference;
    
    // URL de callback
    @JsonProperty("callback_url")
    @NotBlank(message = "L'URL de callback est obligatoire")
    @Size(max = 500, message = "L'URL ne peut pas dépasser 500 caractères")
    private String callbackUrl;
}