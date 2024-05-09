package roomescape.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.theme.ThemeRequest;
import roomescape.dto.time.TimeRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/test_schema.sql"})
public class ReservationControllerTest {

    @Autowired
    private ReservationController reservationController;

    @Autowired
    private ReservationTimeController timeController;

    @Autowired
    private ReservationThemeController themeController;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("예약 목록 조회 시 200으로 응답한다.")
    @Test
    void reservations() {
        // given
        timeController.createTime(new TimeRequest(LocalTime.parse("10:00")));
        themeController.createTheme(new ThemeRequest("name", "desc", "thumb"));
        reservationController.createReservation(new ReservationRequest("user", LocalDate.parse("2025-01-01"), 1L, 1L));

        // when & then
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("정상적인 예약 추가 요청 시 201로 응답한다.")
    @Test
    void insert() {
        // given
        timeController.createTime(new TimeRequest(LocalTime.parse("10:00")));
        themeController.createTheme(new ThemeRequest("name", "desc", "thumb"));

        // when
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", LocalDate.now().plusDays(2).toString());
        params.put("timeId", 1);
        params.put("themeId", 1);

        // then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1))
                .body("name", is("브라운"))
                .body("time.startAt", is("10:00"))
                .body("theme.name", is("name"));
    }

    @DisplayName("예약 삭제 요청 시 204로 응답한다.")
    @Test
    void deleteById() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("이름이 입력되지 않으면 400으로 응답한다.")
    @Test
    void invalidName() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "");
        params.put("date", "2024-04-30");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("이름이 입력되지 않았습니다."));
    }

    @DisplayName("날짜가 입력되지 않으면 400으로 응답한다.")
    @Test
    void invalidDate() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "test");
        params.put("date", "");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("날짜가 입력되지 않았습니다."));
    }

    @DisplayName("시간이 입력되지 않으면 400으로 응답한다.")
    @Test
    void invalidTimeId() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "test");
        params.put("date", "2024-04-30");
        params.put("timeId", "시간 입력");
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("시간이 입력되지 않았습니다."));
    }

    @DisplayName("테마가 입력되지 않으면 400으로 응답한다.")
    @Test
    void invalidThemeId() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "test");
        params.put("date", "2024-04-30");
        params.put("timeId", 1);
        params.put("themeId", "테마 입력");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("테마가 입력되지 않았습니다."));
    }
}
