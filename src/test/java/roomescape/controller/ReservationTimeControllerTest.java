package roomescape.controller;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.dto.web.ReservationTimeWebRequest;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Sql(scripts = "/truncate.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class ReservationTimeControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("성공: 예약 시간 저장 -> 201")
    @Test
    void create() {
        ReservationTimeWebRequest request = new ReservationTimeWebRequest(1L, "10:00");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/times")
            .then().log().all()
            .statusCode(201)
            .body("id", is(1))
            .body("startAt", is("10:00"));
    }

    @DisplayName("성공: 예약 시간 삭제 -> 204")
    @Test
    void deleteBy() {
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES ('10:00')");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .when().delete("/times/1")
            .then().log().all()
            .statusCode(204);
    }

    @DisplayName("성공: 예약 시간 조회 -> 200")
    @Test
    void getReservationTimes() {
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES ('10:00'), ('11:00')");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .when().get("/times")
            .then().log().all()
            .statusCode(200)
            .body("id", contains(1, 2))
            .body("startAt", contains("10:00", "11:00"));
    }

    @DisplayName("실패: 잘못된 포맷의 예약 시간 저장 -> 400")
    @Test
    void create_IllegalTimeFormat() {
        ReservationTimeWebRequest request = new ReservationTimeWebRequest(1L, "24:00");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/times")
            .then().log().all()
            .statusCode(400);
    }

    @DisplayName("예약이 존재하는 시간 삭제 -> 400")
    @Test
    void delete_ReservationExists() {
        jdbcTemplate.update("""
            INSERT INTO reservation_time (start_at) VALUES ('10:00');
            INSERT INTO theme (name, description, thumbnail) VALUES ('n', 'd', 'https://');
            INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('n', '2060-01-01', 1, 1);
            """);

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .when().delete("/times/1")
            .then().log().all()
            .statusCode(400);
    }

    @DisplayName("실패: 이미 존재하는 시간을 저장 -> 400")
    @Test
    void create_Duplicate() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:00')");

        ReservationTimeWebRequest request = new ReservationTimeWebRequest(1L, "10:00");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/times")
            .then().log().all()
            .statusCode(400);
    }
}

