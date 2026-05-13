package roomescape.time.fixture;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.util.HashMap;
import java.util.Map;

public class ReservationTimeApiFixture {

    private ReservationTimeApiFixture() {
    }

    public static Integer createReservationTime(String startAt) {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", startAt);

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(200)
                .extract()
                .path("id");
    }

    public static void updateTimeStatus(Integer timeId, boolean isActive) {
        Map<String, Object> updateActive = new HashMap<>();
        updateActive.put("isActive", isActive);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(updateActive)
                .when().patch("/admin/times/" + timeId + "/status")
                .then().log().all()
                .statusCode(200);
    }

}
