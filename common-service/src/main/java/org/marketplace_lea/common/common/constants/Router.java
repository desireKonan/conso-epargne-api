package org.marketplace_lea.common.common.constants;

public interface Router {
    /* ALL PROJECT BASE URI */
    String ADS_URI = "/ads";
    String ACCESS_URI = "/access";
    String COLLECTS_URI = "/collects";
    String INVESTMENTS_URI = "/investments";
    String CUSTOMERS_URI = "/customers";
    String DASHBOARD_URI = "/dashboard";
    String ENSIGNS_URI = "/ensigns";
    String USER_GROUP_URI = "/groups";
    String LOCALITIES_URI = "/localities";
    String LOGIN_URI = "/login";
    String LOGOUT_URI = "/logout";
    String INDEX_URI = "/login";
    String INVOICE_URI = "/invoices";
    String ORDERS_URI = "/orders";
    String PARTNER_URI = "/partners";
    String PRODUCTS_URI = "/products";
    String SUB_URI = "/sub"; // Ajout Orlando

    String DISTRICTS_URI = "/districts";
    String ENSIGN_PRODUCTS_URI = ENSIGNS_URI + "/{ensId}" + PRODUCTS_URI;
    String PAYMENT_METHODS_URI = "/payment-methods";

    //     String SETTINGS_URI="/settings";

    String SHELVES_URI = "/shelves";
    String ENSIGN_SHELVES_URI = ENSIGNS_URI + "/{ensId}" + SHELVES_URI;
    String USERS_URI = "/users";
    String VOUCHER_URI = "/vouchers";
    String ACCOUNT_TYPES_URI = "/account-types";
    String PROJECT_TYPES_URI = "/project-types";
    String TRANSACTIONS_URI = "/transactions";
    String WITHDRAWALS_URI = "/withdrawals";
    String SUPPLIERS_URI = "/suppliers";

    /* ALL PROJECT BASE URI */


    /* API URI */
    String API_ROOT_V1 = "/api/v1";

    String API_ROOT_V2 = "/api/v2";

    String CONSOM_URI = API_ROOT_V1 + "/consom";
    String CONSOM_SEND_URI = "/send";
    String CONSOM_SELL_URI = "/sell";
    String CONSOM_BUY_URI = "/buy";
    String CONSOM_BUY_LIST_URI = "/listeventesconsom";
    String CONSOM_GET_URI = "/get-by-id";
    String CONSOM_PTOL_URI = "/points-to-lea";
    String CONSOM_GET_TOTAL = "/totals";
    String CONSOM_PEOPLE_WITH_LIST_URI = "/people-with-consom";

    String NOWALLET_CALLBACK_URI = API_ROOT_V1 + "/nowallet-callback";
    String NOWALLET_TRANS_URI = API_ROOT_V1 + "/nowallet-conso";

    String WAVE_TRANS_URI = API_ROOT_V1 + "/wave-conso";

    String API_AUTHENTICATION_URI = "/authenticate";
    String API_REGISTRATION_URI = "/register";
    String API_LOCALITIES_URI = "/localities";
    String PRIVILEGE_URI = "/privileges";
    String ME = "/me";
    String WALLET = ME + "/wallet";
    String CHILDREN = ME + "/children";
    String PAYMENT_GATEWAY_URI = "/payment-gateway";
    String PAYMENT_GATEWAY_REDIRECT_NO_CONTENT = String.format("redirect:%s/transactions/no-content", PAYMENT_GATEWAY_URI);
    String DELIVERY_DATES_URI = "/delivery-dates";
    String CONFIG_URI = "/configs";
    String MEMBERSHIP_URI = "/memberships";
    String NOTIFICATIONS_URI = "/notifications";
    String API_VERIFY_URI = "/verify";
    String API_VERIFY_OTP = API_VERIFY_URI + "/otp";
    String API_VERIFY_CODE = API_VERIFY_URI + "/code";
    String API_VERIFY_LOGIN = API_VERIFY_URI + "/login";
    String CART_URI = "/cart";
    String CART_TO_WISHLIST_URI = CART_URI + "/toWishlist";
    String WISHLIST_URI = "/wishlist";
    String WISHLIST_TO_CART_URI = WISHLIST_URI + "/toCart";

    String AUTHENTICATION_URI = API_ROOT_V1 + API_AUTHENTICATION_URI;
    String REGISTRATION_URI = API_ROOT_V1 + API_REGISTRATION_URI;
    String API_PAYMENT_METHODS_URI = API_ROOT_V1 + PAYMENT_METHODS_URI;
    String API_TRANSACTION_STATUS_URI = API_ROOT_V1 + TRANSACTIONS_URI;
    String ADDRESSES_URI = "/addresses";
    String FAQ_AND_CONDITIONS_URI = "/faq-and-conditions";
    String FAQ = "/faq";
    String TERMS_AND_CONDITIONS = "/terms-and-conditions";
    //Base uri to launch mobile app on phone
    String MLINK_URI = "/mlink";
    String COUNTRIES_URI = "/countries";
    String DELETE_ACCOUNT = "/delete-account";
    String PASSWORD_URI = "/password";
    String FORGOT_PASSWORD_URI = PASSWORD_URI + "/forgot";
    String RESET_PASSWORD = PASSWORD_URI + "/reset";
    String DILAAC_WEBHOOK_EVENT_URI = "/dilaac/events";
    String WAVE_WEBHOOK_SESSION_URI = "/wave-session/events";

    /* API URI */
    String MOBILE_SUCCESS_URL = "https://consoepargneapp.page.link/?apn=com.consoepargne.app&ibi=com.consoepargne.app&link=https://consoepargneapp.page.link/code-success";
    String MOBILE_FAIL_URL = "https://consoepargneapp.page.link/?apn=com.consoepargne.app&ibi=com.consoepargne.app&link=https://consoepargneapp.page.link/code-fail";


    static String redirectTo(String uri) {
        return String.format("redirect:%s%s", DASHBOARD_URI, uri);
    }
}
