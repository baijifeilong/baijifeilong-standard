package io.github.baijifeilong.standard.api.domain;

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

    @Test
    public void testFailure() {
        ApiFailure apiFailure = ApiFailure.of(404, "Not Found");
        log.info("apiFailure = \n{}", apiFailure);
        assert apiFailure.getCode() == 404;
        assert apiFailure.getMessage().equals("Not Found");
    }

    @Test
    public void testPage() {
        ApiPage apiPage = ApiPage.of(stringPage);
        log.info("apiPage = \n{}", apiPage);
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
        log.info("apiContextPage = \n{}", apiContextPage);
        assert apiContextPage.getItems().size() == 3;
        assert apiContextPage.getStartIndex().equals(1111);
        assert apiContextPage.getPreviousIndex().equals(1111);
        assert apiContextPage.getNextIndex().equals(9999);
    }

    @Test
    public void testSuccess() {
        ApiSuccess<String> apiSuccess = ApiSuccess.of("OK");
        log.info("apiSuccess = \n{}", apiSuccess);
        assert apiSuccess.getData().equals("OK");

        ApiSuccess<ApiPage<String>> apiSuccessOfPage = ApiSuccess.ofPage(stringPage);
        log.info("apiSuccessOfPage = \n{}", apiSuccessOfPage);
        assert apiSuccessOfPage.getData().getPageIndex() == 3;
        assert apiSuccessOfPage.getData().getItemsPerPage() == 3;
        assert apiSuccessOfPage.getData().getTotalItems() == 10;
        assert apiSuccessOfPage.getData().getTotalPages() == 10 / 3 + 1;
        assert apiSuccessOfPage.getData().getCurrentItemCount() == 3;
        assert apiSuccessOfPage.getData().getItems().size() == 3;

        ApiSuccess<ApiContextPage<String>> apiSuccessOfContextPage = ApiSuccess.ofContextPage(stringPage.getContent(), 1111, 9999);
        log.info("apiSuccessOfContextPage = \n{}", apiSuccessOfContextPage);
        assert apiSuccessOfContextPage.getData().getItems().size() == 3;
        assert apiSuccessOfContextPage.getData().getStartIndex().equals(1111);
        assert apiSuccessOfContextPage.getData().getPreviousIndex().equals(1111);
        assert apiSuccessOfContextPage.getData().getNextIndex().equals(9999);
    }
}