package roomescape.reservation;

import static org.hamcrest.Matchers.is;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.auth.dto.LoginRequestDto;
import roomescape.member.dto.MemberRequestDto;
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

        RestAssured.given()
                .log().all()
                .body(new MemberRequestDto("hotea@hotea.com", "1234", "hotea"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .when().post("/signup")
                .then().statusCode(201);

        String token = RestAssured.given()
                .log().all()
                .body(new LoginRequestDto("1234", "hotea@hotea.com"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .when().post("/login")
                .then().statusCode(200).extract().cookie("token");

        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(LocalDate.MAX.toString(), 1, 1);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .body(reservationRequestDto)
                .when().post("/reservations")
                .then().statusCode(201);

        RestAssured.given()
                .when().get("/reservations")
                .then().statusCode(200)
                .body("size()", is(1));
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
    void duplicateSave() {
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

        RestAssured.given()
                .log().all()
                .body(new MemberRequestDto("hotea@hotea.com", "1234", "hotea"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .when().post("/signup")
                .then().statusCode(201);

        String token = RestAssured.given()
                .log().all()
                .body(new LoginRequestDto("1234", "hotea@hotea.com"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .when().post("/login")
                .then().statusCode(200).extract().cookie("token");

        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(LocalDate.MAX.toString(), 1, 1);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .body(reservationRequestDto)
                .when().post("/reservations")
                .then().statusCode(201);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .body(reservationRequestDto)
                .when().post("/reservations")
                .then().statusCode(400);
    }

    @Test
    void delete() {
        save();

        RestAssured.given()
                .when().delete("/reservations/1")
                .then().statusCode(200);
    }
}
