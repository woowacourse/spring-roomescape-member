package roomescape.acceptance;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dto.AdminReservationSaveRequest;
import roomescape.dto.MemberReservationSaveRequest;

class ReservationAcceptanceTest extends ApiAcceptanceTest {

    @Test
    @DisplayName("사용자가 예약을 성공적으로 생성하면 201을 응답한다.")
    void respondCreatedWhenCreateReservation() {
        final Long timeId = saveReservationTime();
        final Long themeId = saveTheme();
        final MemberReservationSaveRequest request = new MemberReservationSaveRequest("2034-05-08", timeId, themeId);
        final String accessToken = getAccessToken("mia@email.com");

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("관리자가 예약을 성공적으로 생성하면 201을 응답한다.")
    void respondCreatedWhenAdminCreateReservation() {
        final Long timeId = saveReservationTime();
        final Long themeId = saveTheme();
        final AdminReservationSaveRequest request = new AdminReservationSaveRequest(1L, "2034-05-08", timeId, themeId);
        final String accessToken = getAccessToken("nyangin@email.com");

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간으로 예약 생성 시 400을 응답한다.")
    void respondBadRequestWhenNotExistingReservationTime() {
        saveReservationTime();
        final Long themeId = saveTheme();
        final MemberReservationSaveRequest request = new MemberReservationSaveRequest("2034-05-08", 2L, themeId);
        final String accessToken = getAccessToken("nyangin@email.com");

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("존재하지 않는 테마로 예약 생성 시 400을 응답한다.")
    void respondBadRequestWhenNotExistingTheme() {
        saveTheme();
        final Long timeId = saveReservationTime();
        final MemberReservationSaveRequest request = new MemberReservationSaveRequest("2034-05-08", timeId, 2L);
        final String accessToken = getAccessToken("nyangin@email.com");

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 목록을 성공적으로 조회하면 200을 응답한다.")
    void respondOkWhenFindReservations() {
        final Long timeId = saveReservationTime();
        final Long themeId = saveTheme();
        saveReservation(timeId, themeId);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }
    
    @Test
    @DisplayName("테마, 사용자, 예약 날짜로 예약 목록을 성공적으로 조회하면 200을 응답한다.")
    void respondOkWhenFilteredFindReservations() {
        final Long timeId = saveReservationTime();
        final Long themeId = saveTheme();
        saveReservation(timeId, themeId);
        final String accessToken = getAccessToken("nyangin@email.com");

        RestAssured.given().log().all()
                .queryParam("themeId", 1L)
                .queryParam("memberId", 1L)
                .queryParam("dateFrom", "2034-05-01")
                .queryParam("dateTo", "2034-05-08")
                .cookie("token", accessToken)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }
    
    @Test
    @DisplayName("예약을 성공적으로 삭제하면 204를 응답한다.")
    void respondNoContentWhenDeleteReservation() {
        final Long timeId = saveReservationTime();
        final Long themeId = saveTheme();
        final Long reservationId = saveReservation(timeId, themeId);

        RestAssured.given().log().all()
                .when().delete("/reservations/" + reservationId)
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("존재하지 않는 예약을 삭제하면 400을 응답한다.")
    void respondBadRequestWhenDeleteNotExistingReservation() {
        final Long timeId = saveReservationTime();
        final Long themeId = saveTheme();
        saveReservation(timeId, themeId);
        final Long notExistingReservationTimeId = 2L;

        RestAssured.given().log().all()
                .when().delete("/reservations/" + notExistingReservationTimeId)
                .then().log().all()
                .statusCode(400);
    }
}
