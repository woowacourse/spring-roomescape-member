package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.ReservationTime;
import roomescape.dto.TimeMemberResponse;
import roomescape.dto.TimeResponse;
import roomescape.repository.repositoryImpl.JdbcTimeDao;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TimeRestControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JdbcTimeDao jdbcTimeDao;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("모든 시간을 조회한다.")
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
        assertThat(reservations).containsExactly(new TimeResponse(1L, LocalTime.of(10, 0)));
    }

    //    @Test // TODO: 해결
    void getAll_member() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "11:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "브라운",
                "2099-12-31", 1, 1);

        // when
        List<TimeMemberResponse> reservations = RestAssured.given().log().all()
                .when().get("/times/member?date=2099-12-31?themeId=1")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", TimeMemberResponse.class);

        // then
        assertThat(reservations).containsExactly(
                new TimeMemberResponse(1L, LocalTime.of(10, 0), true),
                new TimeMemberResponse(2L, LocalTime.of(11, 0), false)
        );
    }

    @DisplayName("시간을 생성한다.")
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

        ReservationTime time = jdbcTimeDao.findById(1);

        // then
        assertThat(time.getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @DisplayName("해당 id의 시간을 삭제한다.")
    @Test
    void delete() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");

        // when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        List<ReservationTime> allTimes = jdbcTimeDao.findAll();

        // then
        assertThat(allTimes).isEmpty();
    }
}
