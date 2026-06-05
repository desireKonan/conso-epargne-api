package org.marketplace_lea.common.dtos;

import lombok.Data;

@Data
public class CustomerDTO {
    private String id;
    private AccountDTO account;
    private String firstName;
    private String lastName;
    private String email;

    // private List<PhoneNumberDTO> phoneNumbers;
    //
    // private List<AddressDTO> addresses;

    @Override
    public String toString() {
        return "{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
