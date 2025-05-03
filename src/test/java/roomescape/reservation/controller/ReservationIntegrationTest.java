package roomescape.reservation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.global.config.TestConfig;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.time.controller.dto.AvailableTimeResponse;

@Import(TestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("예약 목록의 조회 시 DB에 저장된 예약 목록을 반환한다")
    @Test
    void get_reservations_test() {
        // when
        List<ReservationResponse> reservations = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationResponse.class);

        // then
        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);

        List<String> names = jdbcTemplate.query("SELECT name FROM reservation", (rs, rowNum) -> rs.getString("name"));

        assertThat(reservations.size()).isEqualTo(count);
        assertThat(reservations)
                .extracting(ReservationResponse::name)
                .containsExactlyElementsOf(names);
    }

    @DisplayName("예약을 생성하면 DB에 예약 데이터가 저장된다")
    @Test
    void add_reservation_test() {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2026-08-05");
        params.put("timeId", "6");
        params.put("themeId", "2");

        // when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(17));

        // then
        String name = jdbcTemplate.queryForObject("SELECT name FROM reservation WHERE id = ?", String.class, 17L);

        assertThat(name).isEqualTo("브라운");
    }

    @DisplayName("예약을 삭제하면 DB의 예약 데이터가 삭제된다")
    @Test
    void delete_reservation_test() {
        // when
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        // then
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(15));

        Boolean actual = jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM reservation WHERE id = ?)",
                Boolean.class, 1);

        assertThat(actual).isFalse();
    }

    @DisplayName("특정 날짜와 테마에 대해 이용가능한 시간 목록을 반환한다")
    @Test
    void get_available_times_test() {
        // when
        List<AvailableTimeResponse> availableTimes = RestAssured.given().log().all()
                .queryParam("date", "2025-04-25")
                .queryParam("themeId", "1")
                .when().get("/reservations/available")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", AvailableTimeResponse.class);

        // then
        List<Long> bookedTimeIds = availableTimes.stream()
                .filter(AvailableTimeResponse::alreadyBooked)
                .map(AvailableTimeResponse::timeId)
                .toList();

        assertThat(bookedTimeIds).containsExactly(2L, 3L);
    }

}
