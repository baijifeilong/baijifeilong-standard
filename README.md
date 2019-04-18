# BaijifeilongStandard [中文版](README_zh-CN.md)

## Standards

### 1. Standard for api responses

- `io.github.baijifeilong.standard.api.domain.ApiFailure.of`
- `io.github.baijifeilong.standard.api.domain.ApiSuccess.of`
- `io.github.baijifeilong.standard.api.domain.ApiSuccess.ofPage`
- `io.github.baijifeilong.standard.api.domain.ApiSuccess.ofContextPage`

#### 1.1 Response for failure

```json
{
  "code" : 404,
  "message" : "Not Found"
}
```

#### 1.2 Response for success

```json
{
  "data" : "OK"
}
```

#### 1.3 Response for success of page

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

#### 1.4 Response for success of context page

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

### 2. Standard for business exceptions

A base class for business exceptions.

- `io.github.baijifeilong.standard.exception.BizException.BizException(java.lang.Throwable, java.lang.Object...)`
- `io.github.baijifeilong.standard.exception.BizException.BizException(java.lang.Object...)`

Features:

- Support status code (Integer)
- Support default code
- Support default message
- Support message template

## Usage

### 1. POM for Maven project

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

### 2. Demonstrations

### 2.1 Api standard usage in SpringBoot application

```java
package bj;

import io.github.baijifeilong.standard.api.domain.ApiFailure;
import io.github.baijifeilong.standard.api.domain.ApiSuccess;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public Object onException(Throwable throwable) {
        return ApiFailure.of(500, throwable.getMessage());
    }
}
```

### 2.2 Exception standard demonstration

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
