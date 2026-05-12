package roomescape.acceptancetest.reservationtime;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import roomescape.acceptancetest.RoomecapeAcceptanceTest;

@RoomecapeAcceptanceTest
class ReservationTimeAdminApiTest {

    @Test
    void 테마_시간_관리_API() {
        Map<String, String> theme = new HashMap<>();
        theme.put("name", "미술관의 밤");
        theme.put("description", "추리 테마");
        theme.put("thumbnailUrl", "https://example.com/theme.png");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(theme)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201);

        Map<String, String> time = new HashMap<>();
        time.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/admin/themes/1/times")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        RestAssured.given().log().all()
                .when().get("/admin/themes/1/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/admin/themes/times/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/admin/themes/1/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

}
