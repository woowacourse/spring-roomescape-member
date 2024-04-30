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
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.ReservationResponse;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationController reservationController;

    @BeforeEach
    void init() {
        RestAssured.port = port;
        jdbcTemplate.update("INSERT INTO reservation_times (start_at) VALUES ('10:00')");
    }

    @DisplayName("존재하지 않는 예약 삭제")
    @Test
    void deletedReservationNotFound() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(404);
    }

    @DisplayName("비어 있는 예약 목록 조회")
    @Test
    void getReservationsWhenEmpty() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @DisplayName("데이터 삽입 후 예약 목록 조회")
    @Test
    void getReservationsAfterInsert() {
        jdbcTemplate.update("INSERT INTO reservations (name, date, time_id) VALUES (?, ?, ?)", "브라운", "2023-08-05", 1L);

        final List<ReservationResponse> reservations = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationResponse.class);

        final Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservations", Integer.class);
        assertThat(reservations.size()).isEqualTo(count);
    }

    @DisplayName("예약 추가 및 삭제")
    @Test
    void saveAndDeleteReservation() {
        final Map<String, Object> params = Map.of(
                "name", "브라운",
                "date", "2023-08-05",
                "timeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/reservations/1");

        final Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservations", Integer.class);
        assertThat(count).isEqualTo(1);

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        final Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from reservations", Integer.class);
        assertThat(countAfterDelete).isEqualTo(0);
    }

    @DisplayName("존재하지 않는 시간으로 예약 추가")
    @Test
    void reservationTimeForSaveNotFound() {
        final Map<String, Object> params = Map.of(
                "name", "브라운",
                "date", "2023-08-05",
                "timeId", 100L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404);
    }

    @DisplayName("컨트롤러에서 jdbcTemplate 필드 제거")
    @Test
    void jdbcTemplateNotInjected() {
        boolean isJdbcTemplateInjected = false;

        for (Field field : reservationController.getClass().getDeclaredFields()) {
            if (field.getType().equals(JdbcTemplate.class)) {
                isJdbcTemplateInjected = true;
                break;
            }
        }

        assertFalse(isJdbcTemplateInjected);
    }
}
