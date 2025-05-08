package roomescape.reservation.presentation.fixture;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public final class ApiHelper {

    public static final String THEME_ENDPOINT = "/themes";
    public static final String TIME_ENDPOINT = "/times";
    public static final String RESERVATION_ENDPOINT = "/reservations";

    public static Response post(final String endpoint, final Object body) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post(endpoint);
    }
}
