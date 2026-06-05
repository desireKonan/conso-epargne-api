package org.marketplace_lea.common.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "SupplierV2")
@Table(name = "ce_supplier_v2")
public class SupplierV2Entity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "second_phone_number", nullable = false)
    private String secondPhoneNumber;

    @Column(name = "address", nullable = false)
    private String address;

    public String retrieveFullname() {
        return lastname + " " + firstname;
    }
}
