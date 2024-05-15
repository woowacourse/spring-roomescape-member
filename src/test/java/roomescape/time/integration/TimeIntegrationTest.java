package roomescape.time.integration;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.model.IntegrationTest;
import roomescape.time.dto.TimeRequest;

public class TimeIntegrationTest extends IntegrationTest {

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
