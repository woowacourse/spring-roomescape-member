package roomescape.acceptance;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import roomescape.support.AcceptanceTest;
import roomescape.support.SimpleRestAssured;
import roomescape.support.annotation.FixedClock;
import roomescape.ui.controller.dto.ReservationRequest;

@FixedClock(date = "2023-08-04")
class ReservationAcceptanceTest extends AcceptanceTest {
    private static final String PATH = "/reservations";

    @TestFactory
    List<DynamicTest> 예약_등록_조회_삭제() {
        return Arrays.asList(
                dynamicTest("예약을 등록한다.", () -> {
                    createTheme();
                    createReservationTime();
                    ReservationRequest request = new ReservationRequest(LocalDate.parse("2023-08-05"), 1L, 1L);
                    SimpleRestAssured.post(PATH, request, userToken())
                            .statusCode(201)
                            .body("id", is(1));
                }),
                dynamicTest("등록된 예약을 조회한다.", () -> {
                    SimpleRestAssured.get(PATH, userToken())
                            .statusCode(200)
                            .body("size()", is(1));
                }),
                dynamicTest("등록된 예약을 삭제한다.", () -> {
                    SimpleRestAssured.delete(PATH + "/1", userToken())
                            .statusCode(204);
                }),
                dynamicTest("삭제 후 예약을 모두 조회한다.", () -> {
                    SimpleRestAssured.get(PATH, userToken())
                            .statusCode(200)
                            .body("size()", is(0));
                })
        );
    }
}
