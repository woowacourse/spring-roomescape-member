package roomescape.reservation.presentation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/truncate.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ReservationTimeControllerTest {
    @LocalServerPort
    private int port;

    @MockitoBean(enforceOverride = true)
    Clock clock;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUpClock() {
        RestAssured.port = port;
        given(clock.getZone()).willReturn(ZoneId.of("Asia/Seoul"));
        given(clock.instant()).willReturn(Instant.parse("2026-05-01T14:00:00Z"));
    }

    @Test
    void 시간_관리_API() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "14:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(5));

        deleteTable();

        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 특정날짜와_테마에_예약_가능_시간을_조회_API() {
        Map<String, Object> options = new HashMap<>();
        options.put("date", "2026-05-05");
        options.put("themeId", 1);

        RestAssured.given().log().all()
                .params(options)
                .when().get("/times/availability")
                .then().log().all()
                .body("size()", is(4))
                .body("[0].isAvailable", is(false))
                .body("[1].isAvailable", is(false))
                .body("[2].isAvailable", is(true))
                .body("[3].isAvailable", is(true))
                .statusCode(200);
    }

    @Test
    @DisplayName("예약 가능 시간 조회 및 예약 생성 이후 예약 가능 시간을 재조회를 할 수 있다.")
    void 정상_흐름_테스트() {
        Map<String, Object> options = new HashMap<>();
        options.put("date", "2026-05-05");
        options.put("themeId", 2);

        RestAssured.given().log().all()
                .params(options)
                .when().get("/times/availability")
                .then().log().all()
                .body("size()", is(4))
                .body("[0].isAvailable", is(false))
                .body("[1].isAvailable", is(false))
                .body("[2].isAvailable", is(false))
                .body("[3].isAvailable", is(true))
                .statusCode(200);

        //5. 예약 생성
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2026-05-05");
        reservation.put("timeId", 4);
        reservation.put("themeId", 2);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        //6. 특정날짜와_테마에_예약_가능_시간을_조회
        Map<String, Object> options1 = new HashMap<>();
        options1.put("date", "2026-05-05");
        options1.put("themeId", 2);

        RestAssured.given().log().all()
                .params(options1)
                .when().get("/times/availability")
                .then().log().all()
                .body("size()", is(4))
                .body("[0].isAvailable", is(false))
                .body("[1].isAvailable", is(false))
                .body("[2].isAvailable", is(false))
                .body("[3].isAvailable", is(false))
                .statusCode(200);
    }

    @Test
    void 예약이_참조하는_시간인_경우_삭제할_수_없다() {
        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(409)
                .body("code", is("RESERVATION_TIME_DELETE_CONFLICT"))
                .body("message", is("이미 예약에 사용 중인 시간대는 삭제할 수 없습니다."));
    }

    private void deleteTable() {
        jdbcTemplate.update("DELETE FROM schedule");
        jdbcTemplate.update("DELETE FROM reservation");
    }
}
