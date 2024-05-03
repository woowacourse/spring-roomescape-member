package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.controller.request.ReservationTimeRequest;
import roomescape.model.ReservationTime;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeControllerTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @DisplayName("모든 예약 시간을 조회한다")
    @Test
    void should_get_reservation_times() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) values (?)", "10:00");

        List<ReservationTime> reservationTimes = RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationTime.class);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation_time", Integer.class);

        Assertions.assertThat(reservationTimes).hasSize(count);
    }

    @DisplayName("예약 시간을 추가한다")
    @Test
    void should_add_reservation_times() {
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(12, 0));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/times/3");

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation_time", Integer.class);
        assertThat(count).isEqualTo(3);
    }

    @DisplayName("예약 시간을 삭제한다")
    @Test
    void should_remove_reservation_time() {
        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation_time", Integer.class);
        assertThat(count).isEqualTo(1);
    }
}
