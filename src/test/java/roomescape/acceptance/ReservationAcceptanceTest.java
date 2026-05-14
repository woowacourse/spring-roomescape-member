package roomescape.acceptance;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ReservationAcceptanceTest extends AcceptanceTestSupport{

    @Test
    @DisplayName("예약 요청에 예약자 이름이 누락된 경우 400 상태코드와 검증 실패 메시지를 반환한다.")
    void blankNameReservationRequestTest() {

        Map<String, Object> params = new HashMap<>();
        params.put("name", "");
        params.put("date", LocalDate.now().plusDays(1).toString());
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("예약자 이름은 필수입니다."));
    }

    @Test
    @DisplayName("예약 요청에 예약 날짜가 누락된 경우 400 상태코드와 검증 실패 메시지를 반환한다.")
    void nullDateReservationRequestTest() {

        Map<String, Object> params = new HashMap<>();
        params.put("name", "타스");
        params.put("date", null);
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("예약 날짜는 필수입니다."));
    }

    @Test
    @DisplayName("예약 요청에 지난 날짜가 예약 날짜로 입력된 경우 400 상태코드와 검증 실패 메시지를 반환한다.")
    void pastDateReservationRequestTest() {

        Map<String, Object> params = new HashMap<>();
        params.put("name", "타스");
        params.put("date", LocalDate.now().minusDays(1).toString());
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("지나간 날짜, 시간에 대한 예약 생성은 불가능합니다."));
    }
}
