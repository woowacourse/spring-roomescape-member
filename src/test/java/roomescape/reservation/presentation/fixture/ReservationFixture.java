package roomescape.reservation.presentation.fixture;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;

public class ReservationFixture {

    public Map<String, String> createReservationRequest(String name, String date, String themeId, String timeId) {
        Map<String, String> reservationParams = new HashMap<>();
        reservationParams.put("name", name);
        reservationParams.put("date", date);
        reservationParams.put("themeId", themeId);
        reservationParams.put("timeId", timeId);
        return reservationParams;
    }

    public Map<String, String> createReservationTimeRequest(String startAt) {
        Map<String, String> reservationTimeParams = new HashMap<>();
        reservationTimeParams.put("startAt", startAt);
        return reservationTimeParams;
    }

    public Map<String, String> createThemeRequest(String name, String description, String thumbnail) {
        Map<String, String> themeParams = new HashMap<>();
        themeParams.put("name", name);
        themeParams.put("description", description);
        themeParams.put("thumbnail", thumbnail);
        return themeParams;
    }

    public void createReservation(String name, String date, String themeId, String timeId){
        Map<String, String> reservation = createReservationRequest(name, date, themeId, timeId);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations");
    }

    public void createReservationTime(String startAt){
        Map<String, String> reservationTime = createReservationTimeRequest(startAt);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTime)
                .when().post("/times");
    }

    public void createTheme(String name, String description, String thumbnail){
        Map<String, String> theme = createThemeRequest(name, description, thumbnail);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(theme)
                .when().post("/themes");
    }
}
