package roomescape.fixture;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;

public class ReservationFixture {

    public static Long createAndReturnId(final String date, final long timeId, final long themeId) {
        final Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", date);
        reservation.put("timeId", timeId);
        reservation.put("themeId", themeId);

        final Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations");

        return Long.parseLong(response.then().extract().jsonPath().getString("id"));
    }
}
