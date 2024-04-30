package roomescape;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ReservationTimeControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("시간 생성에 성공하면, 200을 반환한다")
    void return_200_when_reservationTime_create_success() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then()
                .statusCode(200);

        RestAssured.given()
                .when().get("/times")
                .then()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때 400을 반환한다.")
    void return_400_when_reservationTime_create_input_is_invalid() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "");
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then()
                .statusCode(400);
    }

}
