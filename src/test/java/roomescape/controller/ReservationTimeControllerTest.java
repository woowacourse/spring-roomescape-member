package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReservationTimeControllerTest {
    @LocalServerPort
    int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @DisplayName("잘못된 형식의 시간 데이터로 POST 요청을 보낼 시, 400 Bad Request가 발생한다.")
    @Test
    void requestWithMalformedTimeDate() {
        Map<String, String> requestBody = Map.of("startAt", "10;00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }
}
