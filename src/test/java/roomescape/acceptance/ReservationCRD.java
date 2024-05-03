package roomescape.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.Map;

public class ReservationCRD {
    public static Long postReservation(String date, Long timeId, Long themeId, int expectedHttpCode) {
        Map<?, ?> requestBody = Map.of("name", "포비", "date", date, "timeId", timeId, "themeId", themeId);

        Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(expectedHttpCode)
                .extract().response();

        if (expectedHttpCode == 201) {
            return response.jsonPath().getLong("id");
        }

        return null;
    }
}
