package org.marketplace_lea.common.common.constants;

import java.nio.charset.StandardCharsets;

public interface ConsoEpargneConstants {
    String MEDIA_DIR = "media/";
    String ADS_MEDIA_DIR = "ads";
    String COLLECTS_MEDIA_DIR = "collects";
    String INVESTMENT_MEDIA_DIR = "investments";

    String PRODUCTS_MEDIA_DIR = "products";
    String SHELVES_MEDIA_DIR = "shelves";
    String ENSIGNS_MEDIA_DIR = "ensigns";
    String PAYMENT_METHODS_MEDIA_DIR = "payment-methods";

    String PARTNER = "PARTNER";
    String CUSTOMER = "CUSTOMER";

    String SYSTEM_ACCOUNT_ID = "SYSTEM";
    String DEVISE_LEA_CODE = "LEA";
    String DEVISE_POINT_CODE = "POINT";
    String DEVICE_FCFA_CODE = "FCFA";
    String DEFAULT_DEVISE = DEVISE_POINT_CODE;


    String SUPER_ADMIN_GROUP_LABEL = "SUPER_ADMIN";
    String DEFAULT_ADMIN_USERNAME = "sa";
    String DEFAULT_ADMIN_PASSWORD = "sadmin1234";
    String DEFAULT_SUPPLIER_COMPANY_NAME = "CONSO EPARGNE";

    String ACCESS_TOKEN_KEY = "access_token";
    String EXPIRE_AT_KEY = "expire_at";

    String REMEMBER_ME_KEY = "@conso$ep@rgne";
    int ENCRYPTION_STRENGTH = 11;
    int TOKEN_EXPIRATION_DAY = 5;

    String AWSS3_CE_IMAGE_BUCKET_NAME = "consobucketdev";
    String AWSS3_CE_IMAGE_BUCKET_SERVER_URL = "https://consobucketdev.s3.amazonaws.com";
    String AWSS3SERVICE_FILENAME_KEY = "name";
    String AWSS3SERVICE_FILE_URL_KEY = "url";

    // Websocket
    String INTERNAL_MESSAGES_WS = "/internal-messages";
    String MESSAGES_WS = "/messages";
    String WS_BROKER = "/topic";
    String WS_APP_PREFIX = "/app";
    String STOMP_ENDPOINT = "/ws";

    String WITHDRAWAL_DISCRIMINATOR_VALUE = "W";
    String DEPOSIT_DISCRIMINATOR_VALUE = "D";
    String PAYMENT_DISCRIMINATOR_VALUE = "P";

    //Conso-Epargne Parameters
    float MIN_VOUCHER_AMOUNT = 5000;
    double DEFAULT_LEA_COIN_TO_CURRENCY_CHANGE_AMOUNT = 500;
    String PARTNER_BONUS_ID = "PARTNER_BONUS";

    /// Wallet Constants.
    // 25 Pts = 0.05 Lea → 1 Pts = 0.002 Lea
    double POINT_MULTIPLIER = 0.002;

    // 25 Pts = 1 ConsoM
    int CONSOM_TO_POINTS = 25;


    // Conso-Epargne Savings Account Fees
    int MAX_DEPTHS_ACCOUNT = 1;

    //Payment API
    String PAYMENT_API_URL = "https://smartpay.sotelsportal.ci/api/Transactionsts/Payment";
    String PAYMENT_API_HEADER_X_API_KEY = "4187B07D52F8F5936E876C5600EC957864119BE88356134D4D367B0F129C4B2D";
    String PAYMENT_API_HEADER_X_MERCHANT_ID = "e6a962b6-c56c-46ff-98e9-cd858f8f7d2f";
    String PAYMENT_API_CONTENT_TYPE = "application/json";
    String PAYMENT_API_HEADER_PROD_ENV = "production";
    String PAYMENT_API_HEADER_TEST_ENV = "";

    // Important for payment.
    String GATEWAY_URL_KEY = "gatewayUrl";

    String CORRELATION_ID_HEADER = "X-Correlation-Id";

    String ALGORITHM_WEBHOOK = "HmacSHA256";

    String WAVE_SIGNATURE_HEADER = "Wave-Signature";
}
