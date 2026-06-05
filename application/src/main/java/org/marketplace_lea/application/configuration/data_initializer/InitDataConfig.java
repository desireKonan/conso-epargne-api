package org.marketplace_lea.application.configuration.data_initializer;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class InitDataConfig {
    private List<AccountTypeDef> accountTypes = new ArrayList<>();
    private List<PaymentMethodDef> paymentMethods = new ArrayList<>();
    private List<String> resources = new ArrayList<>();      // Optionnel, peut être utilisé pour générer les privilèges
    private List<PrivilegeDef> privileges = new ArrayList<>();
    private UserGroupDef userGroup;
    private AdminUserDef adminUser;
    private SupplierDef supplier;
    private List<CurrencyDef> currencies = new ArrayList<>();
    private CurrencyConfigDef currencyConfig;
    private AccountDef systemAccount;

    @Data
    public static class AccountTypeDef {
        private String name;
        private String label;
        private int percentage;
        private int cashback;
    }

    @Data
    public static class PaymentMethodDef {
        private String name;
        private boolean active;
        private String provider;
        private boolean isOnline;
    }

    @Data
    public static class PrivilegeDef {
        private String name;
        private String description;
    }

    @Data
    public static class UserGroupDef {
        private String label;
        private String description;
        private List<String> privileges;   // Liste des noms de privilèges
    }

    @Data
    public static class AdminUserDef {
        private String username;
        private String password;
        private String lastName;
        private String firstName;
        private String group;   // label du groupe
    }

    @Data
    public static class SupplierDef {
        private String companyName;
        private String address;
        private String firstName;
        private String lastName;
        private String phoneNumber;
    }


    @Data
    public static class CurrencyDef {
        private String code;
        private String name;
        private String description;
    }


    @Data
    public static class CurrencyConfigDef {
        private int defaultCurrency;
    }


    @Data
    public static class AccountDef {
        private String id;
        private String affiliationCode;
        private String accountTypeId;
        private boolean checked;
    }
}