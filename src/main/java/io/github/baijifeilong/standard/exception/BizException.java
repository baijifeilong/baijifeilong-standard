package io.github.baijifeilong.standard.exception;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Created by BaiJiFeiLong@gmail.com at 2019-04-18 16:05
 */
@SuppressWarnings("WeakerAccess")
public class BizException extends RuntimeException {
    public static final int DEFAULT_CODE = 10000;
    public static final String DEFAULT_MESSAGE = "未知错误";
    private Object[] args;

    public int getCode() {
        return DEFAULT_CODE;
    }

    protected String getTemplate() {
        return "%s";
    }

    protected String getDefaultMessage() {
        return DEFAULT_MESSAGE;
    }

    @Override
    public String getMessage() {
        if (args.length == 0) return getDefaultMessage();
        int countOfPercentSign = StringUtils.countOccurrencesOf(getTemplate().replace("%%", ""), "%");
        Assert.isTrue(countOfPercentSign == args.length, "模板与参数串不匹配");
        return String.format(getTemplate(), args);
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }

    public BizException(Throwable throwable, Object... args) {
        super(throwable);
        this.args = args;
    }

    public BizException(Object... args) {
        super();
        this.args = args;
    }

    @Override
    public String toString() {
        return String.format("%s(%d:%s)", getClass().getSimpleName(), getCode(), getMessage());
    }
}
