package roomescape.controller;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.controller.dto.CreateReservationRequest;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Sql(scripts = "/truncate.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class ReservationControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void beforeEach() {
        jdbcTemplate.update("""
            INSERT INTO reservation_time (start_at) VALUES ('10:00');
            INSERT INTO theme (name, description, thumbnail) VALUES ('n', 'd', 'https://');
            """);
    }

    @DisplayName("성공: 예약 저장 -> 201")
    @Test
    void reserve() {
        CreateReservationRequest request = new CreateReservationRequest("brown", "2060-01-01", 1L, 1L);

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/reservations")
            .then().log().all()
            .statusCode(201)
            .body("id", is(1))
            .body("name", is("brown"))
            .body("date", is("2060-01-01"))
            .body("time.id", is(1))
            .body("theme.id", is(1));
    }

    @DisplayName("성공: 예약 삭제 -> 204")
    @Test
    void deleteBy() {
        jdbcTemplate.update(
            "INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('brown', '2060-01-01', 1, 1)");

        RestAssured.given().log().all()
            .when().delete("/reservations/1")
            .then().log().all()
            .statusCode(204);
    }

    @DisplayName("성공: 예약 조회 -> 200")
    @Test
    void getReservations() {
        jdbcTemplate.update("""
            INSERT INTO reservation (name, date, time_id, theme_id)
            VALUES
            ('brown', '2060-01-01', 1, 1),
            ('tre', '2060-01-02', 1, 1)
            """);

        RestAssured.given().log().all()
            .when().get("/reservations")
            .then().log().all()
            .statusCode(200)
            .body("size()", is(2))
            .body("id", contains(1, 2))
            .body("name", contains("brown", "tre"))
            .body("time.id", contains(1, 1))
            .body("theme.id", contains(1, 1));
    }

    @DisplayName("실패: 존재하지 않는 time id 예약 -> 400")
    @Test
    void reserve_TimeIdNotFound() {
        CreateReservationRequest request = new CreateReservationRequest("tre", "2060-01-01", 2L, 1L);

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/reservations")
            .then().log().all()
            .statusCode(400);
    }

    @DisplayName("실패: 존재하지 않는 theme id 예약 -> 400")
    @Test
    void reserve_ThemeIdNotFound() {
        CreateReservationRequest request = new CreateReservationRequest("tre", "2060-01-01", 1L, 2L);

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/reservations")
            .then().log().all()
            .statusCode(400);
    }

    @DisplayName("실패: 중복 예약 -> 400")
    @Test
    void reserve_Duplication() {
        jdbcTemplate.update(
            "INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('brown', '2060-01-01', 1, 1)");

        CreateReservationRequest request = new CreateReservationRequest("tre", "2060-01-01", 1L, 1L);

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/reservations")
            .then().log().all()
            .statusCode(400);
    }

    @DisplayName("실패: 과거 시간 예약 -> 400")
    @Test
    void reserve_PastTime() {
        CreateReservationRequest request = new CreateReservationRequest("brown", "2000-01-01", 1L, 1L);

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/reservations")
            .then().log().all()
            .statusCode(400);
    }
}
