package org.marketplace_lea.common.dtos;

public record ResponseCallbackDto<T>(String message, T data) {

    public static <T> ResponseCallbackDto<T> success(String successMessage, T data) {
        return new ResponseCallbackDto<>(successMessage, data);
    }

    public static <T> ResponseCallbackDto<T> fail(String successMessage, T data) {
        return new ResponseCallbackDto<>(successMessage, data);
    }
}
