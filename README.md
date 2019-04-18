# BaijifeilongStandard

## 1. Standard for api responses

### 1.1  Functions

- `io.github.baijifeilong.standard.api.domain.ApiFailure.of`
- `io.github.baijifeilong.standard.api.domain.ApiSuccess.of`
- `io.github.baijifeilong.standard.api.domain.ApiSuccess.ofPage`
- `io.github.baijifeilong.standard.api.domain.ApiSuccess.ofContextPage`

### 1.2 Examples

#### 1.2.1 Response for failure

```json
{
  "code" : 404,
  "message" : "Not Found"
}
```

#### 1.2.2 Response for success

```json
{
  "data" : "OK"
}
```

#### 1.2.3 Response for success of page

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

#### 1.2.4 Response for success of context page

```json
{
  "data" : {
    "items" : [ "foo", "bar", "baz" ],
    "startIndex" : 1111,
    "previousIndex" : 1111,
    "nextIndex" : 9999
  }
}
```

## Usage

### 1. POM for Maven project

```xml
<project>
    <dependencies>
        <dependency>
            <groupId>io.github.baijifeilong</groupId>
            <artifactId>baijifeilong-standard</artifactId>
            <version>1.1.1.RELEASE</version>
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

### 2. Usage in SpringBoot application

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
