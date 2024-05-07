package roomescape.acceptance;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.boot.test.mock.mockito.SpyBean;
import roomescape.application.ReservationTimeService;
import roomescape.support.AcceptanceTest;
import roomescape.support.SimpleRestAssured;

public class ExceptionHandlerAcceptanceTest extends AcceptanceTest {
    @SpyBean
    private ReservationTimeService reservationTimeService;

    @TestFactory
    List<DynamicTest> handleExceptionTest() {
        return Arrays.asList(
                dynamicTest("[MethodArgumentNotValidException] 예약 시간을 등록할 때 startAt이 null이다", () -> {
                    Map<String, String> body = Collections.emptyMap();
                    SimpleRestAssured.post("/times", body)
                            .statusCode(400)
                            .body("message", is("입력이 잘못되었습니다."))
                            .body("fieldErrors[0].field", is("startAt"))
                            .body("fieldErrors[0].rejectedValue", is(IsNull.nullValue()))
                            .body("fieldErrors[0].reason", is("시작 시간은 필수입니다."));
                }),
                dynamicTest("[HttpMessageNotReadableException] 예약 시간을 등록할 때 startAt을 LocalTime으로 파싱할 수 없다", () -> {
                    Map<String, String> body = Map.of("startAt", "10:70");
                    SimpleRestAssured.post("/times", body)
                            .statusCode(400)
                            .body("message", is("잘못된 데이터 형식입니다."));
                }),
                dynamicTest("예약 시간을 성공적으로 등록한다", () -> {
                    Map<String, String> body = Map.of("startAt", "10:00");
                    SimpleRestAssured.post("/times", body)
                            .statusCode(201);
                }),
                dynamicTest("[DuplicateKeyException] 중복된 예약 시간을 등록한다", () -> {
                    Map<String, String> body = Map.of("startAt", "10:00");
                    SimpleRestAssured.post("/times", body)
                            .statusCode(409)
                            .body("message", is("이미 존재하는 데이터입니다."));
                }),
                dynamicTest("[ConstraintViolationException] 예약 시간을 삭제할 때 PathVariable의 id가 양수가 아니다", () -> {
                    SimpleRestAssured.delete("/times/-1")
                            .statusCode(400)
                            .body("message", is("입력이 잘못되었습니다."))
                            .body("violationErrors[0].field", is("id"))
                            .body("violationErrors[0].rejectedValue", is("-1"));
                }),
                dynamicTest("[IllegalArgumentException] 예약 시간을 삭제할 때 존재하지 않는 예약 시간을 삭제한다", () -> {
                    SimpleRestAssured.delete("/times/2")
                            .statusCode(400)
                            .body("message", is("존재하지 않는 예약 시간입니다."));
                }),
                dynamicTest("[RuntimeException] 예약 시간을 조회할 때 알 수 없는 에러가 발생한다", () -> {
                    when(reservationTimeService.findReservationTimes()).thenThrow(new RuntimeException());
                    SimpleRestAssured.get("/times")
                            .statusCode(500)
                            .body("message", is("서버 에러입니다. 관리자에게 문의하세요."));
                })
        );
    }
}
