package roomescape.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.TestFixture.*;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.dto.reservation.ReservationTimeResponse;
import roomescape.dto.reservation.ReservationTimeSaveRequest;

class ReservationTimeAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("예약 시간을 성공적으로 생성하면 201을 응답한다.")
    void respondCreatedWhenCreateReservationTime() {
        final ReservationTimeSaveRequest request = new ReservationTimeSaveRequest(START_AT_SIX);

        final ReservationTimeResponse response = assertPostResponse(request, "/times", 201)
                .extract().as(ReservationTimeResponse.class);

        assertThat(response.startAt()).isEqualTo(START_AT_SIX);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "13-00"})
    @DisplayName("잘못된 형식으로 예약 시간 생성 시 400을 응답한다.")
    void respondBadRequestWhenCreateInvalidReservationTime(final String invalidTime) {
        final ReservationTimeSaveRequest request = new ReservationTimeSaveRequest(invalidTime);

        assertPostResponse(request, "/times", 400);
    }

    @Test
    @DisplayName("예약 시간 목록을 성공적으로 조회하면 200을 응답한다.")
    void respondOkWhenFindReservationTimes() {
        saveReservationTime();

        final JsonPath jsonPath = assertGetResponse("/times", 200)
                .extract().response().jsonPath();

        assertThat(jsonPath.getString("startAt[0]")).isEqualTo(START_AT_SIX);
    }

    @Test
    @DisplayName("예약 시간을 성공적으로 삭제하면 204를 응답한다.")
    void respondNoContentWhenDeleteReservationTime() {
        final Long reservationTimeId = saveReservationTime();

        assertDeleteResponse("/times/", reservationTimeId, 204);
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간을 삭제하면 400을 응답한다.")
    void respondBadRequestWhenDeleteNotExistingReservationTime() {
        saveReservationTime();
        final Long notExistingReservationTimeId = 2L;

        assertDeleteResponse("/times/", notExistingReservationTimeId, 400);
    }

    @Test
    @DisplayName("예약 가능한 시간 목록을 성공적으로 조회하면 200을 응답한다.")
    void respondOkWhenFindAvailableReservationTimes() {
        final String date = DATE_MAY_EIGHTH;
        final Long themeId = saveTheme();
        saveReservationTime();

        final JsonPath jsonPath = RestAssured.given().log().all()
                .queryParam("date", date)
                .queryParam("themeId", themeId)
                .when().get("/times/available")
                .then().log().all()
                .statusCode(200)
                .extract().response().jsonPath();

        assertAll(() -> {
            assertThat(jsonPath.getString("startAt[0]")).isEqualTo(START_AT_SIX);
            assertThat(jsonPath.getBoolean("isReserved[0]")).isEqualTo(false);
        });
    }
}
