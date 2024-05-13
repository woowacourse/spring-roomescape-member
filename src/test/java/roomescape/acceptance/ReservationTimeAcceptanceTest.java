package roomescape.acceptance;

import static org.hamcrest.Matchers.is;
import static roomescape.TestFixture.DATE_MAY_EIGHTH;
import static roomescape.TestFixture.START_AT_SIX;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.dto.reservation.ReservationTimeSaveRequest;

class ReservationTimeAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("예약 시간을 성공적으로 생성하면 201을 응답한다.")
    void respondCreatedWhenCreateReservationTime() {
        final ReservationTimeSaveRequest request = new ReservationTimeSaveRequest(START_AT_SIX);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "13-00"})
    @DisplayName("잘못된 형식으로 예약 시간 생성 시 400을 응답한다.")
    void respondBadRequestWhenCreateInvalidReservationTime(final String invalidTime) {
        final ReservationTimeSaveRequest request = new ReservationTimeSaveRequest(invalidTime);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 시간 목록을 성공적으로 조회하면 200을 응답한다.")
    void respondOkWhenFindReservationTimes() {
        saveReservationTime();

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("예약 시간을 성공적으로 삭제하면 200을 응답한다.")
    void respondNoContentWhenDeleteReservationTime() {
        final Long reservationTimeId = saveReservationTime();

        RestAssured.given().log().all()
                .when().delete("/times/" + reservationTimeId)
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간을 삭제하면 400을 응답한다.")
    void respondBadRequestWhenDeleteNotExistingReservationTime() {
        saveReservationTime();
        final Long notExistingReservationTimeId = 2L;

        RestAssured.given().log().all()
                .when().delete("/times/" + notExistingReservationTimeId)
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 가능한 시간 목록을 성공적으로 조회하면 200을 응답한다.")
    void respondOkWhenFindAvailableReservationTimes() {
        final String date = DATE_MAY_EIGHTH;
        final Long themeId = saveTheme();

        RestAssured.given().log().all()
                .queryParam("date", date)
                .queryParam("themeId", themeId)
                .when().get("/times/available")
                .then().log().all()
                .statusCode(200);
    }
}
