package roomescape.acceptancetest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.acceptancetest.fixture.AcceptanceTestFixture;
import roomescape.reservation.domain.Reservation;

@RoomecapeAcceptanceTest
class ReservationApiTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 예약_조회() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void 예약_추가_및_삭제() {
        AcceptanceTestFixture.createTheme();
        AcceptanceTestFixture.createReservationTime("15:40", 1L);

        Map<String, Object> reservation = AcceptanceTestFixture.reservationRequest("브라운", AcceptanceTestFixture.reservationDate(), 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void DB_조회_API_전환() {
        jdbcTemplate.update("INSERT INTO theme (id, name, description, thumbnail_url) VALUES (?, ?, ?, ?)",
                1L, "미술관의 밤", "추리 테마", "https://example.com/theme.png");
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at, theme_id) VALUES (?, ?, ?)", 1L, "15:40:00",
                1L);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)", "브라운", "2023-08-05", 1L);

        List<Reservation> reservations = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", Reservation.class);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);

        assertThat(reservations.size()).isEqualTo(count);
        assertThat(reservations.get(0).getTime().getId()).isEqualTo(1L);
    }

    @Test
    void DB_추가_삭제_API_전환() {
        AcceptanceTestFixture.createTheme();
        AcceptanceTestFixture.createReservationTime("10:00", 1L);

        Map<String, Object> reservation = AcceptanceTestFixture.reservationRequest("브라운", AcceptanceTestFixture.reservationDate(), 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(count).isEqualTo(1);

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(countAfterDelete).isEqualTo(0);
    }

    @Test
    void 예약과_시간_연결() {
        AcceptanceTestFixture.createTheme();
        AcceptanceTestFixture.createReservationTime("10:00", 1L);

        Map<String, Object> reservation = AcceptanceTestFixture.reservationRequest("브라운", AcceptanceTestFixture.reservationDate(), 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

}
