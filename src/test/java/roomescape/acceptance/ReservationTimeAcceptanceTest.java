package roomescape.acceptance;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.ErrorMessage;

public class ReservationTimeAcceptanceTest extends AcceptanceTestSupport{

    @Test
    @DisplayName("예약 시간 생성 요청에 시작 시간이 누락된 경우 400 상태코드와 검증 실패 메시지를 반환한다.")
    void nullStartTimeReservationTimeRequestTest() {

        Map<String, String> params = new HashMap<>();
        params.put("startAt", null);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(400)
                .body("message", is("시작 시간은 필수입니다."));
    }

    @Test
    @DisplayName("예약 시간 생성 요청에 형식이 맞지 않은 시간이 입력된 경우 400 상태코드와 검증 실패 메시지를 반환한다.")
    void invalidFormatReservationTimeRequestTest() {

        Map<String, String> params = new HashMap<>();
        params.put("startAt", "11:00:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(400)
                .body("message", is(ErrorMessage.INVALID_DATA_FORMAT.getMessage()));
    }
}
