package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.controller.api.ReservationTimeController;
import roomescape.dto.ReservationTimeResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationTimeControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationTimeController reservationTimeController;

    @BeforeEach
    void initPort() {
        RestAssured.port = port;
    }

    @DisplayName("비어 있는 예약 시간 목록 조회")
    @Test
    void getReservationTimesWhenEmpty() {
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @DisplayName("데이터 삽입 후 시간 목록 조회")
    @Test
    void getReservationTimesAfterInsert() {
        jdbcTemplate.update("INSERT INTO reservation_times (start_at) VALUES (?)", "15:40");

        final List<ReservationTimeResponse> reservationTimeResponses = RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationTimeResponse.class);

        final Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation_times", Integer.class);
        assertThat(reservationTimeResponses.size()).isEqualTo(count);
    }

    @DisplayName("예약 시간 추가 및 삭제")
    @Test
    void saveAndDeleteReservationTime() {
        final Map<String, String> params = Map.of("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/times/1");

        final Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation_times", Integer.class);
        assertThat(count).isEqualTo(1);

        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);

        final Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from reservation_times", Integer.class);
        assertThat(countAfterDelete).isEqualTo(0);
    }

    @DisplayName("존재하지 않는 예약 시간 삭제")
    @Test
    void deleteReservationTimeNotFound() {
        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("컨트롤러에서 jdbcTemplate 필드 제거")
    @Test
    void jdbcTemplateNotInjected() {
        boolean isJdbcTemplateInjected = false;

        for (Field field : reservationTimeController.getClass().getDeclaredFields()) {
            if (field.getType().equals(JdbcTemplate.class)) {
                isJdbcTemplateInjected = true;
                break;
            }
        }

        assertFalse(isJdbcTemplateInjected);
    }

    @DisplayName("유효하지 않은 시간 형식 입력")
    @ParameterizedTest
    @ValueSource(strings = {"", "    ", "11:11:11", "25:10"})
    void invalidTimeFormat(final String time) {
        final Map<String, String> params = Map.of("startAt", time);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }
}
