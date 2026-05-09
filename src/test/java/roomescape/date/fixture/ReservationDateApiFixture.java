package roomescape.date.fixture;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.util.HashMap;
import java.util.Map;

public class ReservationDateApiFixture {

    private ReservationDateApiFixture() {
    }

    public static Integer createReservationDate(String date) {
        Map<String, String> params = new HashMap<>();
        params.put("date", date);

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/dates")
                .then().log().all()
                .statusCode(200)
                .extract()
                .path("id");
    }

}
