package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeControllerTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @Sql("/clear.sql")
    void 존재하지_않는_예약_시간을_삭제하면_404를_응답한다() {
        RestAssured.given().log().all()
                .when().delete("/times/0")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    @Sql("/clear.sql")
    void 예약_시간을_추가하고_조회하고_삭제한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "startAt", "10:00",
                        "endAt", "10:30"
                ))
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation_time", Integer.class);
        assertThat(count).isEqualTo(1);

        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @Sql("/clear.sql")
    void 해당_시간에_예약이_있으면_예약_시간_삭제시_409를_응답한다() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at, end_at) VALUES (?, ?)", "10:00", "10:30");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail_url) VALUES (?, ?, ?)", "링", "공포 테마", "http:~");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "브라운", "2026-08-05", "1", "1");

        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(409);
    }
}
