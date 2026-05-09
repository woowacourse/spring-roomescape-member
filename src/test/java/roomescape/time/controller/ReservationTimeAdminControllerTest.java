package roomescape.time.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static roomescape.date.fixture.ReservationDateApiFixture.createReservationDate;
import static roomescape.reservation.fixture.ReservationApiFixture.createReservation;
import static roomescape.theme.fixture.ThemeApiFixture.createTheme;
import static roomescape.time.fixture.ReservationTimeApiFixture.createReservationTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "classpath:truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationTimeAdminControllerTest {

    private final String startAt = "10:00:00";

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("관리자는 예약 시간 목록을 조회한다.")
    void get_reservation_times() {
        RestAssured.given().log().all()
                .when().get("/admin/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("관리자는 예약 시간을 생성한다.")
    void create_reservation_time() {
        Integer timeId = createReservationTime(startAt);

        RestAssured.given().log().all()
                .when().get("/admin/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].id", is(timeId))
                .body("[0].startAt", is(startAt));
    }

    @Test
    @DisplayName("관리자는 예약 시간을 삭제한다.")
    void delete_reservation_time() {
        Integer timeId = createReservationTime(startAt);

        RestAssured.given().log().all()
                .when().delete("/admin/times/" + timeId)
                .then().log().all()
                .statusCode(200)
                .body("id", is(timeId))
                .body("startAt", is(startAt));

        RestAssured.given().log().all()
                .when().get("/admin/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("startAt이 없으면 예약 시간 생성에 실패한다.")
    void create_reservation_time_without_start_at() {
        Map<String, Object> params = new HashMap<>();
        params.put("startAt", null);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("이미 예약된 시간을 관리자가 삭제하는 경우 400 예외가 발생한다.")
    void shouldThrowException_WhenDeleteDate_AboutAlreadyReserved() {
        Integer dateId = createReservationDate(LocalDate.of(2099, 1, 1).toString());
        Integer timeId = createReservationTime("10:00");
        Integer themeId = createTheme("테마1");
        String name = "송송";
        createReservation(name, dateId, timeId, themeId);

        RestAssured.given().log().all()
                .when().delete("/admin/times/" + timeId)
                .then().log().all()
                .statusCode(400);
    }

}
