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

    @Autowired
    private Clock fixedClock;
    
    @LocalServerPort
    int port;

    LocalDate nowDate;
    LocalDate pastDate;
    LocalDate futureDate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        nowDate = LocalDate.now(fixedClock);
        pastDate = nowDate.minusDays(1);
        futureDate = nowDate.plusDays(1);
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
    @DisplayName("관리자 예약 등록 테스트")
    void createReservationByAdmin() {
        Map<String, Object> params = new HashMap<>();
        params.put("username", "관리자");
        params.put("themeId", 1);
        params.put("date", futureDate);
        params.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201)
                .body("username", is("관리자"))
                .body("date", is(futureDate.toString()));
    }

    @Test
    @DisplayName("관리자는 지나간 날짜에 대한 예약을 생성할 수 있다.")
    void createReservationByAdminWithPastDate() {
        Map<String, Object> params = new HashMap<>();
        params.put("username", "관리자");
        params.put("themeId", 1);
        params.put("date", "2000-07-16");
        params.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201)
                .body("username", is("관리자"))
                .body("date", is("2000-07-16"));
    }

    @Test
    @DisplayName("관리자는 오늘 날짜의 지난 시간에 대한 예약을 생성할 수 있다.")
    void createReservationByAdminWithPastTime() {
        String today = LocalDate.now(fixedClock).toString();

        Map<String, Object> params = new HashMap<>();
        params.put("username", "관리자");
        params.put("themeId", 4);
        params.put("date", today);
        params.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201)
                .body("username", is("관리자"))
                .body("date", is(today))
                .body("time.startAt", is("10:00"));
    }

    @Test
    @DisplayName("관리자도 중복된 예약을 생성하면 에러가 발생한다.")
    void createDuplicateReservationByAdminThrowException() {
        String today = LocalDate.now(fixedClock).toString();

        Map<String, Object> params = new HashMap<>();
        params.put("username", "관리자");
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
    @DisplayName("관리자도 유효하지 않은 입력값으로 예약을 생성하면 에러가 발생한다.")
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

    @Test
    @DisplayName("관리자는 예약의 테마, 날짜, 시간을 수정할 수 있다.")
    void updateReservationByAdmin() {
        Map<String, Object> params = new HashMap<>();
        params.put("themeId", 2L);
        params.put("date", futureDate);
        params.put("timeId", 2L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().put("/admin/reservations/1")
                .then().log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("theme.id", is(2))
                .body("date", is(futureDate.toString()))
                .body("time.id", is(2));
    }

    @Test
    @DisplayName("관리자도 존재하지 않는 예약 수정 불가")
    void updateReservationByAdmin_throwsException_whenReservationNotFound() {
        Map<String, Object> params = new HashMap<>();
        params.put("themeId", 1L);
        params.put("date", futureDate);
        params.put("timeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().put("/admin/reservations/9999")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("관리자도 존재하지 않는 테마로 수정 불가")
    void updateReservationByAdmin_throwsException_whenThemeNotFound() {
        Map<String, Object> params = new HashMap<>();
        params.put("themeId", 9999L);
        params.put("date", futureDate);
        params.put("timeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().put("/admin/reservations/1")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("관리자도 존재하지 않는 시간으로 수정 불가")
    void updateReservationByAdmin_throwsException_whenTimeNotFound() {
        Map<String, Object> params = new HashMap<>();
        params.put("themeId", 1L);
        params.put("date", futureDate);
        params.put("timeId", 9999L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().put("/admin/reservations/1")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("같은 테마 + 날짜 + 시간 중복 예약으로 수정 불가")
    void updateReservationByAdmin_throwsException_whenDuplicate() {
        // 이미 2026-05-05 10:00 (1번 시간) 예약이 있음 (id=1, 흑곰)
        Map<String, Object> params = new HashMap<>();
        params.put("themeId", 1L);
        params.put("date", "2026-05-05");
        params.put("timeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().put("/admin/reservations/2")
                .then().log().all()
                .statusCode(409);
    }

    @Test
    @DisplayName("예약 삭제")
    void deleteReservationByAdmin() {
        RestAssured.given().log().all()
                .when().delete("/admin/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("존재하지 않는 예약 삭제 불가")
    void deleteReservationByAdmin_throwsException_whenReservationNotFound() {
        RestAssured.given().log().all()
                .when().delete("/admin/reservations/9999")
                .then().log().all()
                .statusCode(404);
    }
}
