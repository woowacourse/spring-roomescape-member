package roomescape.controller;


import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.TimeRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/truncate.sql", "/mockData.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

public class ReservationTimeControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void 전체_시간_조회_API() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("times.size()", is(14));
    }

    @Test
    public void 테마_별_예약가능한_시간_조회_API() {
        LocalDate date = LocalDate.now().minusDays(2);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .queryParam("date", date.toString())
                .when().get("/times/2")
                .then().log().all()
                .statusCode(200)
                .body("times.size()", is(12));
    }

    @Test
    public void 예약_가능한_시간_삭제_API() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/times/11")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    public void 예약_가능한_시간_추가_API() {
        TimeRequest timeRequest = new TimeRequest(LocalTime.of(8, 0));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(timeRequest)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .body("size()", is(2));
    }
}
