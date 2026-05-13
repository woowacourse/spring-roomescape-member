package roomescape.time.presentation.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ReservationTimeControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("예약 시간 생성 시 빈 body를 보내면 400 Bad Request가 발생한다.")
    void createReservationTime_MissingParameter_Throws400() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body("{}")
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 가능 시간 조회 시 테마 ID나 날짜가 누락되면 400 Bad Request가 발생한다.")
    void getAvailableReservationTime_MissingParameter_Throws400() {
        RestAssured.given().log().all()
                .when().get("/times/available")
                .then().log().all()
                .statusCode(400);
    }
}
