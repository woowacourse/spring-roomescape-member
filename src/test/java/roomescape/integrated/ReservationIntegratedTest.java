package roomescape.integrated;

import static org.hamcrest.CoreMatchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationIntegratedTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUpData() {
        String reservationTimeSetUp = "insert into reservation_time (start_at) values ('10:00')";
        String themeSetUp = "insert into theme (name, description, thumbnail) values ('theme_name', 'theme_description', 'theme_thumbnail')";
        String reservationSetUp = "insert into reservation (name, date, time_id, theme_id) values ('reservation_name', '2025-08-04', 1, 1)";
        jdbcTemplate.update(reservationTimeSetUp);
        jdbcTemplate.update(themeSetUp);
        jdbcTemplate.update(reservationSetUp);
    }

    @Test
    @DisplayName("예약을 생성한다.")
    void createReservation() {
        //given
        String name = "브라운";
        String date = "2025-08-05";

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", name);
        reservation.put("date", date);
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        //when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(2),
                        "name", is(name),
                        "date", is(date),
                        "time.id", is(1),
                        "time.startAt", is("10:00"),
                        "theme.id", is(1),
                        "theme.name", is("theme_name")
                );
    }

    @Test
    @DisplayName("전체 예약을 조회한다.")
    void readAllReservations() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void deleteReservation() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }
}
