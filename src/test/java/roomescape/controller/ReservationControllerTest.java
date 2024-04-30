package roomescape.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReservationControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void reservationPageTest() {
        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void reservationsTest() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void insertTest() throws JsonProcessingException {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2023-08-05");
        params.put("timeId", 1);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(params);

        RestAssured.given().contentType("application/json").body(requestJson).log().all()
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    void deleteByIdTest() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("이름이 입력되지 않으면 400으로 응답한다.")
    @Test
    void invalidNameTest() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "");
        params.put("date", "2024-04-30");
        params.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body(is("이름을 입력하여야 합니다."));
    }

    @DisplayName("날짜가 입력되지 않으면 400으로 응답한다.")
    @Test
    void invalidDateTest() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "test");
        params.put("date", "");
        params.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body(is("날짜를 선택하여야 합니다."));
    }

    @DisplayName("시간이 입력되지 않으면 400으로 응답한다.")
    @Test
    void invalidTimeIdTest() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "test");
        params.put("date", "2024-04-30");
        params.put("timeId", "시간 입력");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }
}
