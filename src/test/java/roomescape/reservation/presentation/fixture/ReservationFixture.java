//package roomescape.reservation.presentation.fixture;
//
//import io.restassured.RestAssured;
//import io.restassured.http.ContentType;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import roomescape.reservation.presentation.dto.ReservationRequest;
//import roomescape.reservation.presentation.dto.ReservationTimeRequest;
//import roomescape.reservation.presentation.dto.ThemeRequest;
//
//public class ReservationFixture {
//
//    public ReservationRequest createReservationRequest(String name, LocalDate date, Long themeId, Long timeId) {
//        return new ReservationRequest(date, name, themeId, timeId);
//    }
//
//    public ReservationTimeRequest createReservationTimeRequest(LocalTime startAt) {
//        return new ReservationTimeRequest(startAt);
//    }
//
//    public ThemeRequest createThemeRequest(String name, String description, String thumbnail) {
//        return new ThemeRequest(name, description, thumbnail);
//    }
//
//    public void createReservation(String name, LocalDate date, Long themeId, Long timeId) {
//        final ReservationRequest reservationRequest = createReservationRequest(name, date, themeId, timeId);
//        RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(reservationRequest)
//                .when().post("/reservations");
//    }
//
//    public void createReservationTime(LocalTime startAt) {
//        final ReservationTimeRequest reservationTimeRequest = createReservationTimeRequest(startAt);
//        RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(reservationTimeRequest)
//                .when().post("/times");
//    }
//
//    public void createTheme(String name, String description, String thumbnail) {
//        final ThemeRequest themeRequest = createThemeRequest(name, description, thumbnail);
//        RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(themeRequest)
//                .when().post("/themes");
//    }
//}
