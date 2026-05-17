package roomescape.api;

import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeControllerTest {

    @DisplayName("API - 예약 시간 등록")
    @Test
    void apiReservationTimeCreate() {
        final String createStartAt = "23:00";
        final Map<String,Object> params = new HashMap<>();
        params.put("startAt", createStartAt);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .body("startAt", equalTo(createStartAt));
    }

    @DisplayName("API - 예약 시간 조회")
    @Test
    void apiReservationTimeRetrieve() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", equalTo(13));
    }

    @DisplayName("API - 예약 시간 삭제")
    @Test
    void apiReservationTimeDelete() {
        final String createStartAt = "23:00";
        final Map<String,Object> params = new HashMap<>();
        params.put("startAt", createStartAt);

        final long id = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .body("startAt", equalTo(createStartAt))
                .extract()
                .jsonPath()
                .getLong("id");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/times/" + id)
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", equalTo(13));
    }


    @DisplayName("API - 예약이 존재하는 시간을 삭제할 수 없다.")
    @Test
    void apiReservationTimeDeleteWithExistingReservation() {
        final String createStartAt = "23:00";
        final Map<String,Object> params = new HashMap<>();
        params.put("startAt", createStartAt);

        final long timeId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .body("startAt", equalTo(createStartAt))
                .extract()
                .jsonPath()
                .getLong("id");

        Map<String, Object> reservationParams = new HashMap<>();
        reservationParams.put("name", "브라운");
        reservationParams.put("date", LocalDate.now().plusDays(1).toString());
        reservationParams.put("timeId", timeId);
        reservationParams.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/times/" + timeId)
                .then().log().all()
                .statusCode(409);
    }

    @DisplayName("API - 이미 존재하는 시간을 등록할 수 없다.")
    @Test
    void apiReservationTimeCreateDuplicate() {
        final String createStartAt = "10:00";
        final Map<String,Object> params = new HashMap<>();
        params.put("startAt", createStartAt);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(409);
    }
}
