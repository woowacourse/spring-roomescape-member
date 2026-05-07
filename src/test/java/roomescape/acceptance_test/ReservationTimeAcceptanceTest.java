package roomescape.acceptance_test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.controller.dto.ReservationTimeCreateRequest;

import java.time.LocalTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("예약 시간 생성 후 목록에서 조회된다")
    public void scenario1() throws JsonProcessingException {
        LocalTime startAt = LocalTime.of(10, 30);
        int reservationTimeId = createReservationTime(new ReservationTimeCreateRequest(startAt));

        given()
                .log().all()
        .when()
                .get("/times")
        .then()
                .log().all()
                .statusCode(200)
                .body("times.id", hasItem(reservationTimeId))
                .body("times.startAt", hasItem(startAt.toString()));
    }

    @Test
    @DisplayName("중복된 예약 시간을 생성하면 400 에러가 발생한다")
    public void scenario2() throws JsonProcessingException {
        LocalTime startAt = LocalTime.of(10, 30);
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(startAt);

        createReservationTime(request); // 1차 생성

        // 중복된 시간 생성 시도 -> 400
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/admin/times")
        .then()
                .log().all()
                .statusCode(400);
    }

    private int createReservationTime(ReservationTimeCreateRequest request) throws JsonProcessingException {
        return given().log().all()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
                .when()
                .post("/admin/times")
                .then().log().all()
                .statusCode(201)
                .body("startAt", equalTo(request.startAt().toString()))
                .extract().path("id");
    }
}
