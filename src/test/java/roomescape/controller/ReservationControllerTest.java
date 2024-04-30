package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("해당 ID를 가진 시간이 존재하지 않는다면 404으로 응답한다.")
    void createReservation_AbsenceId_BadRequest() {

        Map<String, String> params = Map.of(
                "name", "브라운",
                "date", "2023-08-05",
                "timeId", "1"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    void createReservation_invalidReservation_bad_request() {

        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");

        Map<String, String> params = Map.of(
                "name", "",
                "date", "",
                "timeId", ""
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
