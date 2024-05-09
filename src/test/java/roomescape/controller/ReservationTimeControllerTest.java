package roomescape.controller;

import static roomescape.TestFixture.RESERVATION_TIME_PARAMETER_SOURCE;
import static roomescape.TestFixture.TIME_FIXTURE;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.dto.request.ReservationTimeRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationTimeControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("id");
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.update("DELETE FROM member");
    }

    @DisplayName("모든 예약 시간 조회 테스트")
    @Test
    void findAllReservationTime() {
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all().assertThat().statusCode(HttpStatus.OK.value());
    }

    @DisplayName("예약 시간 추가 테스트")
    @Test
    void createReservationTime() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new ReservationTimeRequest(TIME_FIXTURE))
                .when().post("/times")
                .then().log().all().assertThat().statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("잘못된 양식의 시간 입력 시 400을 응답한다.")
    @Test
    void invalidTimeInput() {
        // given
        SqlParameterSource reservationTimeRequest = new MapSqlParameterSource()
                .addValue("startAt", "00:61");
        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTimeRequest)
                .when().post("/times")
                .then().log().all().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("중복된 시간 추가 시도 시, 추가되지 않고 400을 응답한다.")
    @Test
    void duplicateReservationTime() {
        // given
        simpleJdbcInsert.withTableName("reservation_time")
                .execute(RESERVATION_TIME_PARAMETER_SOURCE);
        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new ReservationTimeRequest(TIME_FIXTURE))
                .when().post("/times")
                .then().log().all().assertThat().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("예약 시간 삭제 성공 테스트")
    @Test
    void deleteReservationTImeSuccess() {
        // given
        long id = simpleJdbcInsert.withTableName("reservation_time")
                .executeAndReturnKey(RESERVATION_TIME_PARAMETER_SOURCE)
                .longValue();
        // when & then
        RestAssured.given().log().all()
                .when().delete("/times/" + id)
                .then().log().all().assertThat().statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("예약 시간 삭제 실패 테스트")
    @Test
    void deleteReservationTimeFail() {
        // given
        long invalidId = 0;
        // when & then
        RestAssured.given().log().all()
                .when().delete("/reservations/" + invalidId)
                .then().log().all().assertThat().statusCode(HttpStatus.NOT_FOUND.value());
    }
}
