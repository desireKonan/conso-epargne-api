package org.marketplace_lea.prometheus.domain.customer.forms;

public record CustomerSearchForm(
        String login,
        String email,
        String affiliationCode,
        String accountTypeId
) {

    @Override
    public String toString() {
        return "CustomerSearchForm{" +
                "login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", affiliationCode='" + affiliationCode + '\'' +
                ", accountTypeId='" + accountTypeId + '\'' +
                '}';
    }
}
