package roomescape.reservation.presentation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/truncate.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ReservationControllerTest {
    @LocalServerPort
    private int port;

    @MockitoBean(enforceOverride = true)
    Clock clock;

    @BeforeEach
    void setUpClock() {
        RestAssured.port = port;
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
    void 이름으로_본인_예약_목록을_조회할_수_있다() {
        RestAssured.given().log().all()
                .queryParam("name", "kim")
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2))
                .body("[0].name", is("kim"))
                .body("[1].name", is("kim"))
                .body("[0].time.time", is("10:00"))
                .body("[1].time.time", is("11:00"));
    }

    @Test
    void 이름이_비어있으면_본인_예약_목록_조회에_실패한다() {
        RestAssured.given().log().all()
                .queryParam("name", "")
                .when().get("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("code", is("INVALID_INPUT_VALUE"))
                .body("message", is("공백일 수 없습니다"));
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

    @Test
    void 중복된_예약을_생성하는_경우_예약에_실패한다() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2026-05-05");
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(409)
                .body("code", is("DUPLICATE_RESERVATION"))
                .body("message", is("해당 날짜와 시간, 테마에는 이미 예약이 존재합니다."));
    }
}
