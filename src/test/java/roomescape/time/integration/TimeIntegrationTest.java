package roomescape.time.integration;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.time.dto.TimeRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/data-test.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class TimeIntegrationTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void init() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("시간을 잘 등록하고 삭제하고 확인이 가능하다.")
    void reservationTimePageWorks() {
        TimeRequest timeRequest = new TimeRequest(LocalTime.of(10, 0));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(timeRequest)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(4));

        RestAssured.given().log().all()
                .when().delete("/times/3")
                .then().log().all()
                .statusCode(204);
    }
}
