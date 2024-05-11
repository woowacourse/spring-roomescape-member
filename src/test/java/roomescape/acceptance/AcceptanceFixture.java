package roomescape.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalTime;
import roomescape.application.member.dto.request.MemberLoginRequest;
import roomescape.application.member.dto.request.MemberRegisterRequest;
import roomescape.application.reservation.dto.request.ReservationRequest;
import roomescape.application.reservation.dto.request.ReservationTimeRequest;
import roomescape.application.reservation.dto.request.ThemeRequest;
import roomescape.application.reservation.dto.response.ReservationResponse;
import roomescape.application.reservation.dto.response.ReservationTimeResponse;
import roomescape.application.reservation.dto.response.ThemeResponse;

public class AcceptanceFixture {

    public static ExtractableResponse<Response> registerMember(MemberRegisterRequest request) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/members")
                .then().log().all()
                .extract();
    }

    public static String loginAndGetToken(String email, String password) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new MemberLoginRequest(email, password))
                .when().post("/login")
                .then().log().all()
                .extract()
                .cookie("token");
    }

    public static ThemeResponse createTheme(ThemeRequest request) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/themes")
                .then().log().all()
                .extract()
                .as(ThemeResponse.class);
    }

    public static ReservationTimeResponse createReservationTime(int hour, int minute) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new ReservationTimeRequest(LocalTime.of(hour, minute)))
                .when().post("/times")
                .then().log().all()
                .extract()
                .as(ReservationTimeResponse.class);
    }

    public static ReservationResponse createReservation(String token, ReservationRequest request) {
        return RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .extract()
                .as(ReservationResponse.class);
    }
}
