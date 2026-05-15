package roomescape.reservation.presentation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(scripts = {"/truncate.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ReservationControllerTest {
    @MockitoBean(enforceOverride = true)
    Clock clock;

    @BeforeEach
    void setUpClock() {
        given(clock.getZone()).willReturn(ZoneId.of("Asia/Seoul"));
        given(clock.instant()).willReturn(Instant.parse("2026-05-01T14:00:00Z"));
    }

    @Test
    void 예약_생성() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2026-05-06");
        reservation.put("timeId", 2);
        reservation.put("themeId", 1);

        Integer createdId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getInt("id");

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("id", hasItem(createdId));
    }

    @Test
    void 예약_조회() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(9));
    }

    @Test
    void 예약_추가_및_삭제() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2026-05-06");
        reservation.put("timeId", 2);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(10));

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(10));

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(9));
    }

    @Test
    void 과거_날짜로_예약하는_경우_예약에_실패한다() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2025-05-06");
        reservation.put("timeId", 2);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(422)
                .body("code", is("RESERVATION_DATE_TIME_EXPIRED"))
                .body("message", is("지난 날짜와 시간으로는 예약할 수 없습니다."));
    }

    @Test
    void 금일_날짜이지만_지난_시간으로_예약하는_경우_예약에_실패한다() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2026-05-01");
        reservation.put("timeId", 2);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(422)
                .body("code", is("RESERVATION_DATE_TIME_EXPIRED"))
                .body("message", is("지난 날짜와 시간으로는 예약할 수 없습니다."));
    }
}
