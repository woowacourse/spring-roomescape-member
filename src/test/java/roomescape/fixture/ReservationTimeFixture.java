package roomescape.fixture;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;

public class ReservationTimeFixture {

    public static Long createAndReturnId(final String startAt) {
        final Map<String, String> params = new HashMap<>();
        params.put("startAt", startAt);

        final Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times");

        return Long.parseLong(response.then().extract().jsonPath().getString("id"));
    }
}
