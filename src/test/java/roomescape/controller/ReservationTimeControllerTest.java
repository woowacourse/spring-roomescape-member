package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.dto.request.TimeCreateRequest;
import roomescape.dto.response.AvailableTimeResponse;
import roomescape.dto.response.TimeResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationTimeControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        jdbcTemplate.update("INSERT INTO member(name, email, password) VALUES ('켬미', 'aaa@naver.com', '1111')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "11:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "오리와 호랑이", "오리들과 호랑이들 사이에서 살아남기", "https://image.jpg");
        jdbcTemplate.update("INSERT INTO reservation (date, member_id, time_id, theme_id) VALUES (?, ?, ?, ?)"
                , "2023-08-05", 1, 1, 1);
    }

    @DisplayName("시간 목록을 읽을 수 있다.")
    @Test
    void readTimes() {
        int size = RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getInt("size()");

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation_time", Integer.class);

        assertThat(size).isEqualTo(count);
    }

    @DisplayName("예약 가능한 시간 목록을 읽을 수 있다.")
    @Test
    void readAvailableTimes() {
        List<AvailableTimeResponse> expected = List.of(
                new AvailableTimeResponse(new TimeResponse(1L, LocalTime.of(10, 0)), true),
                new AvailableTimeResponse(new TimeResponse(2L, LocalTime.of(11, 0)), false)
        );
        List<AvailableTimeResponse> response = RestAssured.given().log().all()
                .when().get("/times/available?date=2023-08-05&themeId=1")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", AvailableTimeResponse.class);

        assertThat(response).isEqualTo(expected);
    }

    @DisplayName("시간을 DB에 추가할 수 있다.")
    @Test
    void createTime() {
        TimeCreateRequest params = new TimeCreateRequest(LocalTime.of(12, 0));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/times/3");

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation_time", Integer.class);
        assertThat(count).isEqualTo(3);
    }

    @DisplayName("삭제할 id를 받아서 DB에서 해당 시간을 삭제 할 수 있다.")
    @Test
    void deleteTime() {
        RestAssured.given().log().all()
                .when().delete("/times/2")
                .then().log().all()
                .statusCode(204);

        Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from reservation_time", Integer.class);
        assertThat(countAfterDelete).isEqualTo(1);
    }
}
