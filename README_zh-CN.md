# 白季飞龙的Java开发标准 [English Version](README.md)

白季飞龙的Java开发标准

## 标准列表

### 1. API响应标准

- `io.github.baijifeilong.standard.api.domain.ApiFailure.of`
- `io.github.baijifeilong.standard.api.domain.ApiSuccess.of`
- `io.github.baijifeilong.standard.api.domain.ApiSuccess.ofPage`
- `io.github.baijifeilong.standard.api.domain.ApiSuccess.ofContextPage`

#### 1.1 失败响应

```json
{
  "code" : 404,
  "message" : "Not Found"
}
```

#### 1.2 成功响应

```json
{
  "data" : "OK"
}
```

##### 1.2.1 成功响应带分页

```json
{
  "data" : {
    "pageIndex" : 3,
    "itemsPerPage" : 3,
    "totalItems" : 10,
    "totalPages" : 4,
    "currentItemCount" : 3,
    "items" : [ "foo", "bar", "baz" ]
  }
}
```

##### 1.2.2 成功响应带上下文分页

```json
{
  "data" : {
    "items" : [ "foo", "bar", "baz" ],
    "startIndex" : 1111,
    "previousIndex" : 1111,
    "nextIndex" : 9999,
    "currentItemCount" : 3
  }
}
```

### 2. 业务异常标准

一个业务异常的基类

- `io.github.baijifeilong.standard.exception.BizException.BizException(java.lang.Throwable, java.lang.Object...)`
- `io.github.baijifeilong.standard.exception.BizException.BizException(java.lang.Object...)`

特点:

- 支持整型状态码
- 支持默认状态码
- 支持默认错误消息
- 支持消息模板

## 使用

### 1. 在Maven项目中使用的POM文件示例

```xml
<project>
    <dependencies>
        <dependency>
            <groupId>io.github.baijifeilong</groupId>
            <artifactId>baijifeilong-standard</artifactId>
            <version>1.2.1.RELEASE</version>
        </dependency>
    </dependencies>
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
</project>
```

### 2. 使用演示

### 2.1 API标准演示(SpringBoot应用)

```java
package bj;

import io.github.baijifeilong.standard.api.domain.ApiFailure;
import io.github.baijifeilong.standard.api.domain.ApiSuccess;
import io.github.baijifeilong.standard.exception.BizException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
@RestController
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @RequestMapping("/")
    public Object index() {
        if (ThreadLocalRandom.current().nextBoolean()) {
            throw new RuntimeException("Game Over");
        }
        return ApiSuccess.ofContextPage(new ArrayList<String>() {{
            add("hello");
            add("world");
        }}, 111, 999);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus
    public Object onException(Throwable throwable) {
        return ApiFailure.of(new BizException(throwable.getMessage()));
    }
}
```

### 2.2 异常标准演示

```java
package io.github.baijifeilong.standard.exception;

import org.junit.Test;

/**
 * Created by BaiJiFeiLong@gmail.com at 2019-04-18 16:32
 */
public class BizExceptionTest {

    @Test
    public void testBizExceptionWithoutArguments() {
        BizException bizException = new BizException();
        System.out.println("bizException = " + bizException);
        assert bizException.getCode() == 10000;
        assert bizException.getTemplate().equals("%s");
        assert bizException.getMessage().equals("未知错误");
        assert bizException.getLocalizedMessage().equals("未知错误");
    }

    @Test
    public void testBizExceptionWithOneArgument() {
        BizException bizException = new BizException("风萧萧");
        System.out.println("bizException = " + bizException);
        assert bizException.getCode() == 10000;
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
        assert bizException.getCode() == 10000;
        assert bizException.getCause().getMessage().equals("秋风");
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
```
