package roomescape.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.controller.dto.request.ReservationRequest;
import roomescape.controller.dto.response.ReservationResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationApiE2ETest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setDB() {
        jdbcTemplate.execute("DELETE FROM reservation");
        jdbcTemplate.execute("DELETE FROM reservation_time");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "15:40");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)", "브라운", "2023-08-05", 1L);
    }

    @Test
    @DisplayName("예약 추가 시 예약 정보를 저장하고 200 OK를, 삭제 시 id에 해당하는 예약을 제거하고 200 OK를 응답해야 한다")
    void level3() {
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) values (?)", "11:00");
        ReservationRequest request = new ReservationRequest("브라운", LocalDate.parse("2023-08-05"), 2L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("id", is(2));

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("예약 정보를 DB에 저장할 수 있어야 한다")
    void level5() {
        List<ReservationResponse> reservations = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationResponse.class);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);

        assertThat(reservations.size()).isEqualTo(count);
    }

    @Test
    @DisplayName("DB에 저장된 예약 정보를 추가/삭제 할 수 있어야 한다")
    void level6() {
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) values (?)", "11:00");
        ReservationRequest request = new ReservationRequest("브라운", LocalDate.parse("2023-08-05"), 2L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(count).isEqualTo(2);

        RestAssured.given().log().all()
                .when().delete("/reservations/2")
                .then().log().all()
                .statusCode(200);

        Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(countAfterDelete).isEqualTo(1);
    }

    @Test
    @DisplayName("주어진 API 구조를 기반으로 예약 데이터를 추가해야 한다")
    void level8() {
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) values (?)", "11:00");
        ReservationRequest request = new ReservationRequest("브라운", LocalDate.parse("2023-08-05"), 2L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @DisplayName("존재하지 않는 예약을 삭제하려는 경우 404 Not Found를 던진다")
    @Test
    void reservationRemove() {
        //given
        long notExistId = 100L;

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/reservations/" + notExistId)
                .then().log().all()
                .statusCode(404);
    }
}
