package roomescape.time;

import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.time.dto.ReservationTimeRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void save() {
        ReservationTimeRequestDto requestDto = new ReservationTimeRequestDto("10:00");

        RestAssured.given()
                   .contentType(ContentType.JSON)
                   .body(requestDto)
                   .when().post("/times")
                   .then().statusCode(201);
    }

    @Test
    void findAll() {
        save();
        RestAssured.given()
                   .when().get("/times")
                   .then().statusCode(200)
                   .body("size()", is(1));
    }

    @Test
    void delete() {
        save();
        RestAssured.given()
                   .when().delete("/times/1")
                   .then().statusCode(200);

        RestAssured.given()
                   .when().delete("/times/1")
                   .then().statusCode(204);
    }
}
