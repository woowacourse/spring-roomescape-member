package roomescape.domain.reservation.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {

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
    @DisplayName("예약 목록을 조회한다.")
    void findAllReservations() {
        RestAssured.given().log().all()
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("reservations.size()", is(13))
                .body("reservations[0].id", is(1))
                .body("reservations[0].username", is("흑곰"))
                .body("reservations[0].theme.id", is(1))
                .body("reservations[0].theme.name", is("워너비"))
                .body("reservations[0].theme.description", is("워너비 테마입니다."))
                .body("reservations[0].theme.thumbnailUrl", is("https://example.com/wannabe.png"))
                .body("reservations[0].date", is("2026-05-05"))
                .body("reservations[0].time.id", is(1))
                .body("reservations[0].time.startAt", is("10:00"));
    }

    @Test
    @DisplayName("내 예약 목록을 조회한다.")
    void findMyReservationTest() {
        RestAssured.given().log().all()
                .when().get("/reservations/me/흑곰")
                .then().log().all()
                .statusCode(200)
                .body("reservations.size()", is(1))
                .body("reservations[0].username", is("흑곰"))
                .body("reservations[0].theme.name", is("워너비"))
                .body("reservations[0].date", is("2026-05-05"))
                .body("reservations[0].time.startAt", is("10:00"));
    }

    @Test
    @DisplayName("예약을 생성한다.")
    void createReservation() {
        Map<String, Object> params = new HashMap<>();
        params.put("username", "테스터");
        params.put("themeId", 3);
        params.put("date", LocalDate.of(2026, 5, 8));
        params.put("timeId", 6);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .header("Location", notNullValue())
                .body("id", notNullValue())
                .body("username", is("테스터"))
                .body("theme.id", is(3))
                .body("theme.name", is("우주 정거장"))
                .body("theme.description", is("우주에서 살아남으세요."))
                .body("theme.thumbnailUrl", is("https://example.com/space.png"))
                .body("date", is("2026-05-08"))
                .body("time.id", is(6))
                .body("time.startAt", is("15:00"));
    }

    @Test
    @DisplayName("중복된 예약을 생성하면 에러가 발생한다.")
    void createDuplicateReservationThrowException() {
        Map<String, Object> params = new HashMap<>();
        params.put("username", "테스터");
        params.put("themeId", 1);
        params.put("date", "2026-05-10");
        params.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(409);
    }

    @Test
    @DisplayName("과거 날짜로 예약을 생성하면 에러가 발생한다.")
    void createReservationWithPastDateThrowException() {
        Map<String, Object> params = new HashMap<>();
        params.put("username", "테스터");
        params.put("themeId", 1);
        params.put("date", "2026-05-05");
        params.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("오늘 날짜의 지난 시간으로 예약을 생성하면 에러가 발생한다.")
    void createReservationWithPastTimeThrowException() {
        Map<String, Object> params = new HashMap<>();
        params.put("username", "테스터");
        params.put("themeId", 1);
        params.put("date", "2026-05-06");
        params.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("유효하지 않은 입력값으로 예약을 생성하면 에러가 발생한다.")
    void createReservationWithInvalidInputThrowException() {
        Map<String, Object> params = new HashMap<>();
        params.put("username", "");
        params.put("themeId", 1);
        params.put("date", "2026-05-10");
        params.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약을 수정한다.")
    void updateReservation() {
        Map<String, Object> params = new HashMap<>();
        params.put("date", "2026-05-10");
        params.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().put("/reservations/1")
                .then().log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("date", is("2026-05-10"))
                .body("time.id", is(1));
    }

    @Test
    @DisplayName("과거 날짜로 예약을 수정하면 에러가 발생한다.")
    void updateReservationWithPastDateThrowException() {
        Map<String, Object> params = new HashMap<>();
        params.put("date", "2026-05-05");
        params.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().put("/reservations/1")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("중복된 시간으로 예약을 수정하면 에러가 발생한다.")
    void updateReservationWithDuplicateThrowException() {
        Map<String, Object> params = new HashMap<>();
        params.put("date", "2026-05-05");
        params.put("timeId", 2);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().put("/reservations/1")
                .then().log().all()
                .statusCode(409);
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void deleteReservation() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("지난 예약을 삭제하면 에러가 발생한다.")
    void deletePastReservationThrowException() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(400);
    }
}
