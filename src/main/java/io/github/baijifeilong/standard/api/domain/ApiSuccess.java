package io.github.baijifeilong.standard.api.domain;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by BaiJiFeiLong@gmail.com at 2018/12/28 上午11:07
 * <p>
 * 接口成功响应
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@Getter
public class ApiSuccess<T> {

    private Object data;

    private ApiSuccess(T data) {
        this.data = data;
    }

    public static <T> ApiSuccess<T> of(T t) {
        return new ApiSuccess<>(t);
    }

    public static <T> ApiSuccess<ApiPage<T>> ofPage(Page<T> t) {
        return of(ApiPage.of(t));
    }

    public static <T> ApiSuccess<ApiContextPage<T>> ofContextPage(List<T> items, long previousIndex, long nextIndex) {
        return of(ApiContextPage.of(items, previousIndex, nextIndex));
    }
}