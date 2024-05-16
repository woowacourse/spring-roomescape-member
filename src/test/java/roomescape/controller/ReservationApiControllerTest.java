package roomescape.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.service.dto.ReservationAdminRequest;

@Sql("/truncate.sql")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ReservationApiControllerTest {

    private static final String ADMIN_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbi1pZCIsIm5hbWUiOiJBZG1pbiIsInJvbGUiOiJhZG1pbiJ9.rwrMXggxF2IBybjj7M8kC4XBciVOd8LGChohMzS4T_1RJ1BagnTcf-0r0OLqbZeUBT3OdRHcCtYqDmLOzr80AA";
    @LocalServerPort
    private int port;

    @BeforeEach
    public void init() {
        RestAssured.port = port;
        initializeTimesData();
        initializeThemeData();
    }

    private final ReservationAdminRequest request = new ReservationAdminRequest("3000-04-22", 1L, 1L, 1L);

    @DisplayName("/reservations GET 요청 시 모든 예약 목록과 200 상태 코드를 응답한다.")
    @Test
    void return_200_status_code_and_saved_all_reservations_when_get_request() throws Exception {
        Cookie cookie = makeCookie(ADMIN_TOKEN);

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("memberId", request.getMemberId());
        reservation.put("date", request.getDate());
        reservation.put("timeId", request.getTimeId());
        reservation.put("themeId", request.getThemeId());

        RestAssured.given().log().all()
                .cookie("token", cookie.getValue())
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("/reservations/{id} DELETE 요청 시 204 상태 코드를 응답한다.")
    @Test
    void return_200_status_code_when_delete_request() throws Exception {
        Cookie cookie = makeCookie(ADMIN_TOKEN);

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("memberId", request.getMemberId());
        reservation.put("date", request.getDate());
        reservation.put("timeId", request.getTimeId());
        reservation.put("themeId", request.getThemeId());

        RestAssured.given().log().all()
                .cookie("token", cookie.getValue())
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

    }

    private void initializeThemeData() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "이름");
        params.put("description", "설명");
        params.put("thumbnail", "썸네일");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }

    private static void initializeTimesData() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);
    }

    private jakarta.servlet.http.Cookie makeCookie(String token) {
        jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        return cookie;
    }
}
