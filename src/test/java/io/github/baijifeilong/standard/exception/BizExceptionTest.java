package io.github.baijifeilong.standard.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.baijifeilong.standard.api.domain.ApiFailure;
import lombok.SneakyThrows;
import org.junit.Test;

/**
 * Created by BaiJiFeiLong@gmail.com at 2019-04-18 16:32
 */
public class BizExceptionTest {

    @Test
    public void testBizExceptionWithoutArguments() {
        BizException bizException = new BizException();
        System.out.println("bizException = " + bizException);
        assert bizException.getCode() == BizException.DEFAULT_CODE;
        assert bizException.getTemplate().equals("%s");
        assert bizException.getMessage().equals("未知错误");
        assert bizException.getLocalizedMessage().equals("未知错误");
    }

    @Test
    public void testBizExceptionWithOneArgument() {
        BizException bizException = new BizException("风萧萧");
        System.out.println("bizException = " + bizException);
        assert bizException.getCode() == BizException.DEFAULT_CODE;
        assert bizException.getTemplate().equals("%s");
        assert bizException.getMessage().equals("风萧萧");
        assert bizException.getLocalizedMessage().equals("风萧萧");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBizExceptionWithTwoArguments() {
        BizException bizException = new BizException("风萧萧", "易水寒");
        System.out.println("bizException = " + bizException);
    }

    @Test
    public void testBizExceptionWithThrowable() {
        BizException bizException = new BizException(new RuntimeException("秋风"), "抛物线");
        System.out.println("bizException = " + bizException);
        bizException.printStackTrace();
        assert bizException.getCode() == BizException.DEFAULT_CODE;
        assert bizException.getMessage().equals("抛物线");
        assert bizException.getCause().getMessage().equals("秋风");
    }

    @Test
    public void testBizExceptionWithNullThrowable() {
        BizException bizException = new BizException(new RuntimeException(), "抛物线");
        System.out.println("bizException = " + bizException);
        bizException.printStackTrace();
        assert bizException.getCode() == BizException.DEFAULT_CODE;
        assert bizException.getMessage().equals("抛物线");
        assert bizException.getCause().getMessage() == null;
    }

    @Test
    public void testBizExceptionWithNullThrowableAndNullMessage() {
        BizException bizException = new BizException(new RuntimeException());
        System.out.println("bizException = " + bizException);
        bizException.printStackTrace();
        assert bizException.getCode() == BizException.DEFAULT_CODE;
        assert bizException.getMessage().equals(BizException.DEFAULT_MESSAGE);
        assert bizException.getCause().getMessage() == null;
    }

    @Test
    public void testLoginExceptionWithoutArguments() {
        LoginException loginException = new LoginException();
        System.out.println("loginException = " + loginException);
        assert loginException.getCode() == 11000;
        assert loginException.getMessage().equals("登录失败: 未知错误");
    }

    @Test
    public void testLoginExceptionWithOneArgument() {
        LoginException loginException = new LoginException("用户已被禁用");
        System.out.println("loginException = " + loginException);
        assert loginException.getCode() == 11000;
        assert loginException.getMessage().equals("登录失败: 用户已被禁用");
    }

    @Test
    public void testUsernameNotExist() {
        LoginException.UsernameNotExist usernameNotExist = new LoginException.UsernameNotExist("foo");
        System.out.println("usernameNotExist = " + usernameNotExist);
        assert usernameNotExist.getCode() == 11001;
        assert usernameNotExist.getMessage().equals("登录失败: 用户名(foo)不存在");
    }

    @Test
    @SneakyThrows
    public void testApiFailure() {
        LoginException loginException = new LoginException("系统维护中");
        ApiFailure apiFailure = ApiFailure.of(loginException);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(apiFailure);
        System.out.println(json);
        assert apiFailure.getCode() == 11000;
        assert apiFailure.getMessage().equals("登录失败: 系统维护中");
    }
}

class LoginException extends BizException {
    LoginException(Object... args) {
        super(args);
    }

    @Override
    public int getCode() {
        return 11000;
    }

    @Override
    protected String getTemplate() {
        return "登录失败: %s";
    }

    @Override
    protected String getDefaultMessage() {
        return String.format(getTemplate(), "未知错误");
    }

    static class UsernameNotExist extends LoginException {
        UsernameNotExist(Object... args) {
            super(args);
        }

        @Override
        public int getCode() {
            return 11001;
        }

        @Override
        protected String getTemplate() {
            return String.format(super.getTemplate(), "用户名(%s)不存在");
        }
    }
}
