package roomescape.fixture;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import roomescape.domain.ReservationTime;
import roomescape.service.dto.input.ReservationTimeInput;

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

    public static ReservationTimeInput getInput() {
        return new ReservationTimeInput("10:00");
    }

    public static ReservationTime getDomain(final String startAt) {
        return ReservationTime.of(null, startAt);
    }

    public static ReservationTime getDomain() {
        return getDomain("10:00");
    }
}
