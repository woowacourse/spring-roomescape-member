package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationApiTest {

    @Test
    void 예약_조회_빈목록() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void 예약_추가() {
        Integer timeId = createTime("11:00");

        Map<String, Object> params = new HashMap<>();
        params.put("name", "민욱");
        params.put("date", "2026-08-05");
        params.put("timeId", timeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", is("민욱"))
                .body("date", is("2026-08-05"));
    }

    @Test
    void 예약_추가_후_조회() {
        Integer timeId = createTime("14:00");

        Map<String, Object> params = new HashMap<>();
        params.put("name", "티뉴");
        params.put("date", "2026-09-01");
        params.put("timeId", timeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].name", is("티뉴"));
    }

    @Test
    void 예약_추가_및_삭제() {
        Integer timeId = createTime("18:00");

        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2026-10-10");
        params.put("timeId", timeId);

        Integer reservationId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().get("id");

        RestAssured.given().log().all()
                .when().delete("/reservations/" + reservationId)
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    private Integer createTime(String startAt) {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", startAt);

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().get("id");
    }
}
