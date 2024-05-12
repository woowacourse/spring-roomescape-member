package roomescape.controller.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.response.ReservationTimeResponse;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/truncate.sql")
class ReservationTimeControllerIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        String insertSql = "INSERT INTO reservation_time(start_at) VALUES (?)";
        jdbcTemplate.update(insertSql, LocalTime.of(9, 0, 0));
        jdbcTemplate.update(insertSql, LocalTime.of(10, 0, 0));
    }

    @AfterEach
    void clearTable() {
        jdbcTemplate.update("DELETE FROM reservation_time");
    }

    @Test
    @DisplayName("새로운 예약시간을 저장을 요청할 수 있다")
    void should_SaveTime_When_Give_TimeRequest() {
        LocalTime requestTime = LocalTime.of(11, 0, 0);
        Map<String, Object> request = new HashMap<>();
        request.put("startAt", requestTime.toString());

        ReservationTimeResponse response = RestAssured.given().log().all()
                .body(request)
                .contentType(ContentType.JSON)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .extract().body().jsonPath().getObject(".", ReservationTimeResponse.class);

        assertThat(response.startAt()).isEqualTo(requestTime);
    }

    @Test
    @DisplayName("예약시간 목록을 반환한다")
    void should_getAllReservationTimes() {
        //given
        String sql = "SELECT count(*) FROM reservation_time";
        int timeCount = jdbcTemplate.queryForObject(sql, Integer.class);

        //when
        List<ReservationTimeResponse> responses = RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .extract().body().jsonPath().getList(".", ReservationTimeResponse.class);

        //then
        List<LocalTime> times = responses.stream()
                .map(ReservationTimeResponse::startAt)
                .toList();

        assertAll(
                () -> assertThat(responses).hasSize(timeCount),
                () -> assertThat(times).containsExactlyInAnyOrder(
                        LocalTime.of(9, 0, 0),
                        LocalTime.of(10, 0)
                )
        );
    }

    @Test
    @DisplayName("예약 시간id를 가진 예약시간 삭제가 가능하다")
    void should_DeleteTime_When_GiveTimeId() {
        //given
        String sql = "SELECT count(*) FROM reservation_time";
        int firstCount = jdbcTemplate.queryForObject(sql, Integer.class);

        //when
        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);

        //then
        int secondCount = jdbcTemplate.queryForObject(sql, Integer.class);
        assertThat(firstCount).isEqualTo(secondCount + 1);
    }
}