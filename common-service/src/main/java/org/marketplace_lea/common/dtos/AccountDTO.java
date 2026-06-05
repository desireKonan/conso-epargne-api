package org.marketplace_lea.common.dtos;

import lombok.Data;

@Data
public class AccountDTO {
    private String id;
    private String login;
    private String affiliationCode;
    private boolean hasLeaParent;
    private AccountTypeDTO accountType;
}
