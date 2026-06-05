package org.marketplace_lea.common.common.utils;

import org.marketplace_lea.common.common.constants.ConsoEpargneConstants;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

public final class MediaUtils {
    public static String createNewFileName() {
        return UUID.randomUUID().toString();
    }

    public static String getProductImageUrl(String idProduct, String image) {
        return buildImageUrlForObject(getProductImageKey(idProduct, image));
    }

    public static String getShelfImageUrl(String image) {
        return buildImageUrlForObject(getShelfImageKey(image));
    }

    public static String getAdImageUrl(String image) {
        return buildImageUrlForObject(getAdImageKey(image));
    }

    public static String getPaymentMethodImageUrl(String image) {
        return buildImageUrlForObject(getPaymentMethodImageKey(image));
    }

    public static String getEnsignImageUrl(String image) {
        return buildImageUrlForObject(getEnsignImageKey(image));
    }

    public static String getCollectImageUrl(String image) {
        return buildImageUrlForObject(getCollectImageKey(image));
    }

    public static String getInvestmentImageUrl(String image) {
        return buildImageUrlForObject(getInvestmentImageKey(image));
    }

    private static String buildImageUrlForObject(String image) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(String.format("/%s", ConsoEpargneConstants.MEDIA_DIR))
                .path(image)
                .toUriString();
    }

    //FILES KEY FROM S3 BUCKET
    public static String getProductImageKey(String idProduct, String image) {
        return String.format("%s/%s/%s", ConsoEpargneConstants.PRODUCTS_MEDIA_DIR, idProduct, image);
    }

    public static String getShelfImageKey(String image) {
        return String.format("%s/%s", ConsoEpargneConstants.SHELVES_MEDIA_DIR, image);
    }

    public static String getPaymentMethodImageKey(String image) {
        return String.format("%s/%s", ConsoEpargneConstants.PAYMENT_METHODS_MEDIA_DIR, image);
    }

    public static String getAdImageKey(String image) {
        return String.format("%s/%s", ConsoEpargneConstants.ADS_MEDIA_DIR, image);
    }

    public static String getEnsignImageKey(String image) {
        return String.format("%s/%s", ConsoEpargneConstants.ENSIGNS_MEDIA_DIR, image);
    }

    public static String getCollectImageKey(String image) {
        return String.format("%s/%s", ConsoEpargneConstants.COLLECTS_MEDIA_DIR, image);
    }

    public static String getInvestmentImageKey(String image) {
        return String.format("%s/%s", ConsoEpargneConstants.INVESTMENT_MEDIA_DIR, image);
    }
}