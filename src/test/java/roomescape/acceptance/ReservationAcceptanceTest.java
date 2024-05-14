package roomescape.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.TestFixture.*;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dto.reservation.AdminReservationSaveRequest;
import roomescape.dto.reservation.MemberReservationSaveRequest;
import roomescape.dto.reservation.ReservationResponse;

class ReservationAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("사용자가 예약을 성공적으로 생성하면 201을 응답한다.")
    void respondCreatedWhenCreateReservation() {
        final Long timeId = saveReservationTime();
        final Long themeId = saveTheme();
        final MemberReservationSaveRequest request = new MemberReservationSaveRequest(DATE_MAY_EIGHTH, timeId, themeId);

        final ReservationResponse response = assertPostResponseWithToken(
                request, MEMBER_MIA_EMAIL, "/reservations", 201)
                .extract().as(ReservationResponse.class);


        assertAll(() -> {
            assertThat(response.name()).isEqualTo(MEMBER_MIA_NAME);
            assertThat(response.date()).isEqualTo(DATE_MAY_EIGHTH);
            assertThat(response.time().id()).isEqualTo(timeId);
            assertThat(response.theme().id()).isEqualTo(themeId);
        });
    }

    @Test
    @DisplayName("관리자가 예약을 성공적으로 생성하면 201을 응답한다.")
    void respondCreatedWhenAdminCreateReservation() {
        final Long timeId = saveReservationTime();
        final Long themeId = saveTheme();
        final AdminReservationSaveRequest request = new AdminReservationSaveRequest(1L, DATE_MAY_EIGHTH, timeId, themeId);

        final ReservationResponse response = assertPostResponseWithToken(
                request, ADMIN_EMAIL, "/admin/reservations", 201)
                .extract().as(ReservationResponse.class);

        assertAll(() -> {
            assertThat(response.name()).isEqualTo(ADMIN_NAME);
            assertThat(response.date()).isEqualTo(DATE_MAY_EIGHTH);
            assertThat(response.time().id()).isEqualTo(timeId);
            assertThat(response.theme().id()).isEqualTo(themeId);
        });
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간으로 예약 생성 시 400을 응답한다.")
    void respondBadRequestWhenNotExistingReservationTime() {
        saveReservationTime();
        final Long themeId = saveTheme();
        final MemberReservationSaveRequest request = new MemberReservationSaveRequest(DATE_MAY_EIGHTH, 2L, themeId);

        assertPostResponseWithToken(request, MEMBER_MIA_EMAIL, "/reservations", 400);
    }

    @Test
    @DisplayName("존재하지 않는 테마로 예약 생성 시 400을 응답한다.")
    void respondBadRequestWhenNotExistingTheme() {
        saveTheme();
        final Long timeId = saveReservationTime();
        final MemberReservationSaveRequest request = new MemberReservationSaveRequest(DATE_MAY_EIGHTH, timeId, 2L);

        assertPostResponseWithToken(request, MEMBER_MIA_EMAIL, "/reservations", 400);
    }

    @Test
    @DisplayName("예약 목록을 성공적으로 조회하면 200을 응답한다.")
    void respondOkWhenFindReservations() {
        saveReservation();

        final JsonPath jsonPath = assertGetResponse("/reservations", 200)
                .extract().response().jsonPath();

        assertAll(() -> {
            assertThat(jsonPath.getString("name[0]")).isEqualTo(MEMBER_MIA_NAME);
            assertThat(jsonPath.getString("date[0]")).isEqualTo(DATE_MAY_EIGHTH);
            assertThat(jsonPath.getString("time[0].startAt")).isEqualTo(START_AT_SIX);
            assertThat(jsonPath.getString("theme[0].name")).isEqualTo(THEME_HORROR_NAME);
        });
    }
    
    @Test
    @DisplayName("테마, 사용자, 예약 날짜로 예약 목록을 성공적으로 조회하면 200을 응답한다.")
    void respondOkWhenFilteredFindReservations() {
        saveReservation();
        final String accessToken = getAccessToken(MEMBER_MIA_EMAIL);

        final JsonPath jsonPath = RestAssured.given().log().all()
                .queryParam("themeId", 1L)
                .queryParam("memberId", 1L)
                .queryParam("dateFrom", "2034-05-01")
                .queryParam("dateTo", "2034-05-08")
                .cookie("token", accessToken)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .extract().response().jsonPath();

        assertAll(() -> {
            assertThat(jsonPath.getString("name[0]")).isEqualTo(MEMBER_MIA_NAME);
            assertThat(jsonPath.getString("date[0]")).isEqualTo(DATE_MAY_EIGHTH);
            assertThat(jsonPath.getString("time[0].startAt")).isEqualTo(START_AT_SIX);
            assertThat(jsonPath.getString("theme[0].name")).isEqualTo(THEME_HORROR_NAME);
        });
    }
    
    @Test
    @DisplayName("예약을 성공적으로 삭제하면 204를 응답한다.")
    void respondNoContentWhenDeleteReservation() {
        final Long reservationId = saveReservation();

        assertDeleteResponse("/reservations/", reservationId, 204);
    }

    @Test
    @DisplayName("존재하지 않는 예약을 삭제하면 400을 응답한다.")
    void respondBadRequestWhenDeleteNotExistingReservation() {
        saveReservation();
        final Long notExistingReservationTimeId = 2L;

        assertDeleteResponse("/reservations/", notExistingReservationTimeId, 400);
    }
}
