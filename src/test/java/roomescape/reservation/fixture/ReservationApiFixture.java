package roomescape.reservation.fixture;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.util.HashMap;
import java.util.Map;

public class ReservationApiFixture {

    private ReservationApiFixture() {
    }

    public static Integer createReservation(String name, Integer dateId, Integer timeId, Integer themeId) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("dateId", dateId);
        params.put("timeId", timeId);
        params.put("themeId", themeId);

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/member/reservations")
                .then().log().all()
                .statusCode(200)
                .extract()
                .path("id");
    }

}
