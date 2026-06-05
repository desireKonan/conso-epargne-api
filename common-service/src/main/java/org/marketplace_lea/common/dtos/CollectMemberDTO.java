package org.marketplace_lea.common.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollectMemberDTO {
    private String lastName;

    private String firstName;

    private String contact;

    private double expectedAmountPerMonth;
}
