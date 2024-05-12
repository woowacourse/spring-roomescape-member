package roomescape.acceptance;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import roomescape.support.AcceptanceTest;
import roomescape.support.SimpleRestAssured;
import roomescape.ui.controller.dto.ThemeRequest;

public class ThemeAcceptanceTest extends AcceptanceTest {
    private static final String PATH = "/themes";

    @TestFactory
    List<DynamicTest> 테마_등록_조회_삭제() {
        return Arrays.asList(
                dynamicTest("테마를 등록한다.", () -> {
                    ThemeRequest request = new ThemeRequest("신밧드의 모험", "설명입니다", "https://www.test.com");
                    SimpleRestAssured.post(PATH, request, adminToken())
                            .statusCode(201);
                }),
                dynamicTest("등록된 테마를 조회한다.", () -> {
                    SimpleRestAssured.get(PATH, adminToken())
                            .statusCode(200)
                            .body("size()", is(1));
                }),
                dynamicTest("등록된 테마를 삭제한다.", () -> {
                    SimpleRestAssured.delete(PATH + "/1", adminToken())
                            .statusCode(204);
                })
        );
    }
}
