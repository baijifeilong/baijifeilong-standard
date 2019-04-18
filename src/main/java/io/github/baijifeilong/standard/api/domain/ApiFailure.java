package io.github.baijifeilong.standard.api.domain;

import io.github.baijifeilong.standard.exception.BizException;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by BaiJiFeiLong@gmail.com at 2018/12/28 上午11:13
 * <p>
 * 接口失败响应
 */
@SuppressWarnings({"unused"})
@Getter
@ToString
public class ApiFailure {

    private int code;
    private String message;

    private ApiFailure(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ApiFailure of(int code, String message) {
        return new ApiFailure(code, message);
    }

    public static ApiFailure of(int code) {
        return new ApiFailure(code, BizException.DEFAULT_MESSAGE);
    }

    public static ApiFailure of(String message) {
        return new ApiFailure(BizException.DEFAULT_CODE, message);
    }

    public static ApiFailure of(BizException bizException) {
        return of(bizException.getCode(), bizException.getMessage());
    }

    public static ApiFailure of(Throwable throwable) {
        if (throwable instanceof BizException) {
            return of((BizException) throwable);
        }
        return of(BizException.DEFAULT_CODE, throwable.getMessage());
    }
}
