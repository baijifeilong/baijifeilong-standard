package io.github.baijifeilong.standard.api.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BaiJiFeiLong@gmail.com at 2019-04-18 13:24
 */
@SuppressWarnings("WeakerAccess")
@Getter
public class ApiContextPage<T> {

    private List<T> items;
    private Object startIndex;
    private Object previousIndex;
    private Object nextIndex;

    private ApiContextPage(List<T> items, Object previousIndex, Object nextIndex) {
        this.items = new ArrayList<>(items);
        this.previousIndex = previousIndex;
        this.nextIndex = nextIndex;
        this.startIndex = this.previousIndex;
    }

    public static <T> ApiContextPage<T> of(List<T> items, Object previousIndex, Object nextIndex) {
        return new ApiContextPage<>(items, previousIndex, nextIndex);
    }
}