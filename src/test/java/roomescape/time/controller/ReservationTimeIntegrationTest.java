package roomescape.time.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
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
import roomescape.time.controller.dto.ReservationTimeResponse;

@Import(TestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("예약 시간 목록 조회 시 DB에 저장된 예약 시간 목록을 반환한다")
    @Test
    void get_times_test() {
        // when
        List<ReservationTimeResponse> times = RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationTimeResponse.class);

        // then
        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation_time", Integer.class);

        assertThat(times.size()).isEqualTo(count);
    }

    @DisplayName("예약 시간를 생성하면 DB에 예약 시간 데이터가 저장된다")
    @Test
    void add_time_test() {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:20");

        // when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .body("id", is(7));

        // then
        String startAt = jdbcTemplate.queryForObject("SELECT start_at FROM reservation_time WHERE id = ?", String.class,
                7L);
        assertThat(startAt).isEqualTo("10:20");
    }

    @DisplayName("예약 시간을 삭제하면 DB의 예약 시간 데이터가 삭제된다")
    @Test
    void delete_time_test() {
        // when
        RestAssured.given().log().all()
                .when().delete("/times/6")
                .then().log().all()
                .statusCode(204);

        // then
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM reservation_time", Integer.class);

        Boolean isExist = jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM reservation_time WHERE id = ?)",
                Boolean.class, 6);

        assertAll(
                () -> assertThat(count).isEqualTo(5),
                () -> assertThat(isExist).isFalse()
        );
    }
}
