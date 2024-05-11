package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate-data.sql")
class AdminReservationControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationController reservationController;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("관리자가 예약 추가, 조회를 정상적으로 수행한다.")
    @Sql(scripts = {"/truncate-data.sql", "/member-data.sql"})
    void ReservationTime_CREATE_READ_Success() {
        Map<String, String> time = Map.of(
                "startAt", "10:00"
        );

        Map<String, Object> theme = Map.of(
                "name", "테마",
                "description", "테마 설명",
                "thumbnail", "테마 썸네일"
        );

        String timeLocation = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .extract().header("Location");

        String themeLocation = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(theme)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .extract().header("Location");

        Long timeId = Long.parseLong(timeLocation.substring(timeLocation.lastIndexOf("/") + 1));
        Long themeId = Long.parseLong(themeLocation.substring(themeLocation.lastIndexOf("/") + 1));

        Map<String, Object> reservation = Map.of(
                "date", LocalDate.now().plusDays(1L).toString(),
                "timeId", timeId,
                "themeId", themeId,
                "memberId", 1
        );

        RestAssured.given().log().all()
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
}
