package roomescape.reservation;

import static org.hamcrest.Matchers.is;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.reservation.dto.ReservationRequestDto;
import roomescape.theme.dto.ThemeRequestDto;
import roomescape.time.dto.ReservationTimeRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"/schema.sql"})
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
                   .when().get("/reservations")
                   .then().statusCode(200)
                   .body("size()", is(0));
        save();
        RestAssured.given()
                   .when().get("/reservations")
                   .then().statusCode(200)
                   .body("size()", is(1));
    }

    @Test
    void save() {
        ReservationTimeRequestDto reservationTimeRequestDto = new ReservationTimeRequestDto("10:00");
        RestAssured.given()
                   .contentType(ContentType.JSON)
                   .body(reservationTimeRequestDto)
                   .when().post("/times")
                   .then().statusCode(201);

        ThemeRequestDto themeRequestDto = new ThemeRequestDto("정글모험", "정글모험 설명", "정글모험 이미지");
        RestAssured.given()
                   .contentType(ContentType.JSON)
                   .body(themeRequestDto)
                   .when().post("/themes")
                   .then().statusCode(201);


        ReservationRequestDto reservationRequestDto = new ReservationRequestDto("hi", LocalDate.MAX.toString(), 1, 1);
        RestAssured.given()
                   .contentType(ContentType.JSON)
                   .body(reservationRequestDto)
                   .when().post("/reservations")
                   .then().statusCode(201);

        RestAssured.given()
                   .when().get("/reservations")
                   .then().statusCode(200)
                   .body("size()", is(1));
    }

    @Test
    void duplicateSave() {
        save();
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto("브라운", LocalDate.MAX.toString(), 1, 1);
        RestAssured.given()
                   .contentType(ContentType.JSON)
                   .body(reservationRequestDto)
                   .when().post("/reservations")
                   .then().statusCode(409);
    }

    @Test
    void delete() {
        save();
        RestAssured.given()
                   .when().delete("/reservations/1")
                   .then().statusCode(200);

        RestAssured.given()
                   .when().delete("/reservations/1")
                   .then().statusCode(204);
    }
}
