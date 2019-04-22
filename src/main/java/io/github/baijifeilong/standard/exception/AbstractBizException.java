package io.github.baijifeilong.standard.exception;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Created by BaiJiFeiLong@gmail.com at 2019-04-18 16:05
 */
abstract public class AbstractBizException extends RuntimeException implements IBizException {
    private Object[] args;

    abstract public int getCode();

    protected String getTemplate() {
        return "%s";
    }

    abstract protected String getDefaultMessage();

    @Override
    public String getMessage() {
        if (args.length == 0) return getDefaultMessage();
        int countOfPercentSign = StringUtils.countOccurrencesOf(getTemplate().replace("%%", ""), "%");
        Assert.isTrue(countOfPercentSign == args.length, "模板与参数串不匹配");
        return String.format(getTemplate(), args);
    }

    public AbstractBizException(Throwable throwable, Object... args) {
        super(throwable);
        this.args = args;
    }

    public AbstractBizException(Object... args) {
        super();
        this.args = args;
    }

    @Override
    public String toString() {
        return String.format("%s(%d:%s)", getClass().getSimpleName(), getCode(), getMessage());
    }
}
