package roomescape.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.List;
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

    public static void getReservationTimes(int expectedHttpCode, int expectedReservationsSize) {
        Response response = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(expectedHttpCode)
                .extract().response();

        List<?> reservationTimeResponses = response.as(List.class);

        assertThat(reservationTimeResponses).hasSize(expectedReservationsSize);
    }

    public static void deleteReservationTime(Long reservationId, int expectedHttpCode) {
        RestAssured.given().log().all()
                .when().delete("/reservations/" + reservationId)
                .then().log().all()
                .statusCode(expectedHttpCode);
    }
}
