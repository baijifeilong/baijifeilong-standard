package io.github.baijifeilong.standard.api.domain;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BaiJiFeiLong@gmail.com at 2019-04-18 13:24
 */
@Getter
@ToString
public class ApiRelativePage<T> {

    private List<T> items;
    private Object startIndex;
    private Object previousIndex;
    private Object nextIndex;
    private int currentItemCount;

    private ApiRelativePage(List<T> items, Object previousIndex, Object nextIndex) {
        this.items = new ArrayList<>(items);
        this.previousIndex = previousIndex;
        this.nextIndex = nextIndex;
        this.startIndex = this.previousIndex;
        this.currentItemCount = items.size();
    }

    public static <T> ApiRelativePage<T> of(List<T> items, Object previousIndex, Object nextIndex) {
        return new ApiRelativePage<>(items, previousIndex, nextIndex);
    }
}
