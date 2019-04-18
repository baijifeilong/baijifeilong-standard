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

    public static ApiFailure of(BizException bizException) {
        return of(bizException.getCode(), bizException.getMessage());
    }
}
