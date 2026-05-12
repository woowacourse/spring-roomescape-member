package roomescape.domain.reservation.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminReservationControllerTest {

    @Autowired
    private Clock fixedClock;

    @TestConfiguration
    static class FixedClockConfig {

        @Bean
        @Primary
        Clock fixedClock() {
            return Clock.fixed(
                    LocalDate.of(2026, 5, 6)
                            .atTime(14, 0)
                            .atZone(ZoneId.systemDefault())
                            .toInstant(),
                    ZoneId.systemDefault()
            );
        }
    }

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("어드민은 지나간 날짜에 대한 예약을 생성할 수 있다.")
    void createReservationByAdminWithPastDate() {
        Map<String, Object> params = new HashMap<>();
        params.put("username", "어드민");
        params.put("themeId", 1);
        params.put("date", "2000-07-16");
        params.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201)
                .body("username", is("어드민"))
                .body("date", is("2000-07-16"));
    }

    @Test
    @DisplayName("어드민은 오늘 날짜의 지난 시간에 대한 예약을 생성할 수 있다.")
    void createReservationByAdminWithPastTime() {
        String today = LocalDate.now(fixedClock).toString();

        Map<String, Object> params = new HashMap<>();
        params.put("username", "어드민");
        params.put("themeId", 4);
        params.put("date", today);
        params.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201)
                .body("username", is("어드민"))
                .body("date", is(today))
                .body("time.startAt", is("10:00"));
    }

    @Test
    @DisplayName("어드민도 중복된 예약을 생성하면 에러가 발생한다.")
    void createDuplicateReservationByAdminThrowException() {
        String today = LocalDate.now(fixedClock).toString();

        Map<String, Object> params = new HashMap<>();
        params.put("username", "어드민");
        params.put("themeId", 4);
        params.put("date", today);
        params.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(409);
    }

    @Test
    @DisplayName("어드민도 유효하지 않은 입력값으로 예약을 생성하면 에러가 발생한다.")
    void createReservationByAdminWithInvalidInputThrowException() {
        Map<String, Object> params = new HashMap<>();
        params.put("username", "");
        params.put("themeId", 1);
        params.put("date", "2026-05-10");
        params.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(400);
    }
}
