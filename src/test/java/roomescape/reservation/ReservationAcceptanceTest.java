package roomescape.reservation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.dto.ReservationRequest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void findAll() {
        RestAssured.given()
                .log()
                .all()
                .when()
                .get("/reservations")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("size()", is(0));
        save();
        RestAssured.given()
                .log()
                .all()
                .when()
                .get("/reservations")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void save() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .body(params)
                .when()
                .post("/times")
                .then()
                .log()
                .all()
                .statusCode(201);

        Map<String, String> theme = new HashMap<>();
        theme.put("name", "정글모험");
        theme.put("description", "정글모험 제목");
        theme.put("thumbnail", "정글모험 이미지");

        RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .body(theme)
                .when()
                .post("/themes")
                .then()
                .log()
                .all()
                .statusCode(201);


        ReservationRequest reservationRequest = new ReservationRequest("hi", LocalDate.MAX, 1, 1);

        RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when()
                .post("/reservations")
                .then()
                .log()
                .all()
                .statusCode(201);

        RestAssured.given()
                .log()
                .all()
                .when()
                .get("/reservations")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void duplicateSave() {
        save();
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("reservation_name", "브라운");
        reservation.put("date", LocalDate.MAX.toString());
        reservation.put("timeId", 1);

        RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when()
                .post("/reservations")
                .then()
                .log()
                .all()
                .statusCode(400);
    }

    @Test
    void delete() {
        save();
        RestAssured.given()
                .log()
                .all()
                .when()
                .delete("/reservations/1")
                .then()
                .log()
                .all()
                .statusCode(204);

        RestAssured.given()
                .log()
                .all()
                .when()
                .delete("/reservations/1")
                .then()
                .log()
                .all()
                .statusCode(404);
    }
}
