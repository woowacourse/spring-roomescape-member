package roomescape.acceptance;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import roomescape.support.AcceptanceTest;
import roomescape.support.SimpleRestAssured;

class ReservationTimeAcceptanceTest extends AcceptanceTest {
    private static final String PATH = "/times";
    private static final Map<String, String> BODY = Map.of(
            "startAt", "10:00"
    );

    @DisplayName("[7단계 - 시간 관리 기능]")
    @TestFactory
    List<DynamicTest> step7() {
        return Arrays.asList(
                dynamicTest("시간을 등록한다.", () -> {
                    SimpleRestAssured.post(PATH, BODY)
                            .statusCode(201);
                }),
                dynamicTest("등록된 시간을 조회한다.", () -> {
                    SimpleRestAssured.get(PATH)
                            .statusCode(200)
                            .body("size()", is(1));
                }),
                dynamicTest("시간을 삭제한다.", () -> {
                    SimpleRestAssured.delete(PATH + "/1")
                            .statusCode(204);
                })
        );
    }
}
