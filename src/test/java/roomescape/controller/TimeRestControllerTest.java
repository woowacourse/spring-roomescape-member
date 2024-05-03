package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.TimeMemberResponse;
import roomescape.dto.TimeResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TimeRestControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @Test
    void getAll() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");

        // when
        List<TimeResponse> reservations = RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", TimeResponse.class);

        // then
        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation_time", Integer.class);
        assertThat(reservations.size()).isEqualTo(count);
    }

//    @Test //TODO 완성
    void getAll_available() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "11:00");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "12:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마이름", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "브라운",
                "2024-08-05", 1, 1);

        // when
        List<TimeMemberResponse> reservations = RestAssured.given().log().all()
                .when().get("/times/member?date=2024-05-03?themeId=1")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", TimeMemberResponse.class);

        // then
        assertThat(reservations.size()).isEqualTo(1);
    }

    @ParameterizedTest
    @EmptySource
    void create_invalid_time_bad_request(String input) {
        // given
        Map<String, String> params = Map.of(
                "startAt", input
        );

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void create() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        // when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        // then
        Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from reservation_time", Integer.class);
        assertThat(countAfterDelete).isEqualTo(1);
    }

    @Test
    void deleteById_existReservation_bad_request() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)", "테니", "2024-04-30", 1);

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void deleteById_existTime_bad_request() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        Map<String, String> params = Map.of(
                "startAt", "10:00"
        );

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
