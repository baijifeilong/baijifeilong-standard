package io.github.baijifeilong.standard.api.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;

/**
 * Created by BaiJiFeiLong@gmail.com at 2019-04-18 13:39
 */
@Slf4j
public class ApiDomainTests {

    private Page<String> stringPage = new PageImpl<>(new ArrayList<String>() {{
        add("foo");
        add("bar");
        add("baz");
    }}, PageRequest.of(2, 3), 10);

    private ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    private void dumpIt(Object object) {
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object));
    }

    @Test
    public void testFailure() {
        ApiFailure apiFailure = ApiFailure.of(404, "Not Found");
        dumpIt(apiFailure);
        assert apiFailure.getCode() == 404;
        assert apiFailure.getMessage().equals("Not Found");
    }

    @Test
    public void testPage() {
        ApiPage apiPage = ApiPage.of(stringPage);
        dumpIt(apiPage);
        assert apiPage.getPageIndex() == 3;
        assert apiPage.getItemsPerPage() == 3;
        assert apiPage.getTotalItems() == 10;
        assert apiPage.getTotalPages() == 10 / 3 + 1;
        assert apiPage.getCurrentItemCount() == 3;
        assert apiPage.getItems().size() == 3;
    }

    @Test
    public void testContextPage() {
        ApiContextPage<String> apiContextPage = ApiContextPage.of(stringPage.getContent(), 1111, 9999);
        dumpIt(apiContextPage);
        assert apiContextPage.getItems().size() == 3;
        assert apiContextPage.getStartIndex().equals(1111);
        assert apiContextPage.getPreviousIndex().equals(1111);
        assert apiContextPage.getNextIndex().equals(9999);
    }

    @Test
    public void testSuccess() {
        ApiSuccess<String> apiSuccess = ApiSuccess.of("OK");
        dumpIt(apiSuccess);
        assert apiSuccess.getData().equals("OK");

        ApiSuccess<ApiPage<String>> apiSuccessOfPage = ApiSuccess.ofPage(stringPage);
        dumpIt(apiSuccessOfPage);
        assert apiSuccessOfPage.getData().getPageIndex() == 3;
        assert apiSuccessOfPage.getData().getItemsPerPage() == 3;
        assert apiSuccessOfPage.getData().getTotalItems() == 10;
        assert apiSuccessOfPage.getData().getTotalPages() == 10 / 3 + 1;
        assert apiSuccessOfPage.getData().getCurrentItemCount() == 3;
        assert apiSuccessOfPage.getData().getItems().size() == 3;

        ApiSuccess<ApiContextPage<String>> apiSuccessOfContextPage = ApiSuccess.ofContextPage(stringPage.getContent(), 1111, 9999);
        dumpIt(apiSuccessOfContextPage);
        assert apiSuccessOfContextPage.getData().getItems().size() == 3;
        assert apiSuccessOfContextPage.getData().getStartIndex().equals(1111);
        assert apiSuccessOfContextPage.getData().getPreviousIndex().equals(1111);
        assert apiSuccessOfContextPage.getData().getNextIndex().equals(9999);
    }
}