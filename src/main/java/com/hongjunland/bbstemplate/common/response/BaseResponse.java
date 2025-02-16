package com.hongjunland.bbstemplate.common.response;

public record BaseResponse<T>(boolean success, String message, T data) {
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(true, "요청이 성공적으로 처리되었습니다.", data);
    }

    public static BaseResponse<Void> failure(String message) {
        return new BaseResponse<>(false, message, null);
    }
}

