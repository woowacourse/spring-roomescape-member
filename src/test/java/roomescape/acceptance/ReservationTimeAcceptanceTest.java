package roomescape.acceptance;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import roomescape.support.AcceptanceTest;
import roomescape.support.SimpleRestAssured;
import roomescape.ui.controller.dto.ReservationTimeRequest;

class ReservationTimeAcceptanceTest extends AcceptanceTest {
    private static final String PATH = "/times";

    @TestFactory
    List<DynamicTest> 시간_등록_조회_삭제() {
        return Arrays.asList(
                dynamicTest("시간을 등록한다.", () -> {
                    ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.parse("10:00"));
                    SimpleRestAssured.post(PATH, request, adminToken())
                            .statusCode(201);
                }),
                dynamicTest("등록된 시간을 조회한다.", () -> {
                    SimpleRestAssured.get(PATH, adminToken())
                            .statusCode(200)
                            .body("size()", is(1));
                }),
                dynamicTest("등록된 시간을 삭제한다.", () -> {
                    SimpleRestAssured.delete(PATH + "/1", adminToken())
                            .statusCode(204);
                })
        );
    }
}
