package roomescape.acceptance.admin;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import roomescape.acceptance.BaseAcceptanceTest;
import roomescape.dto.ReservationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.acceptance.fixture.PreInsertedDataFixture.*;

@DisplayName("관리자가 예약을 필터링해서 조회한다.")
class ReservationFilterAcceptanceTest extends BaseAcceptanceTest {

    @Test
    @DisplayName("필터링하지 않음")
    void filterNone() {
        List<ReservationResponse> response = sendRequestFilteringWithToken("");

        assertThat(response).containsExactlyInAnyOrder(
                ReservationResponse.from(PRE_INSERTED_RESERVATION_1),
                ReservationResponse.from(PRE_INSERTED_RESERVATION_2),
                ReservationResponse.from(PRE_INSERTED_RESERVATION_3),
                ReservationResponse.from(PRE_INSERTED_RESERVATION_4)
        );
    }

    @Test
    @DisplayName("사용자로 필터링")
    void filterByMemberId() {
        List<ReservationResponse> response = sendRequestFilteringWithToken("?memberId=2");

        assertThat(response).containsExactlyInAnyOrder(
                ReservationResponse.from(PRE_INSERTED_RESERVATION_1),
                ReservationResponse.from(PRE_INSERTED_RESERVATION_2),
                ReservationResponse.from(PRE_INSERTED_RESERVATION_3)
        );
    }

    @Test
    @DisplayName("테마로 필터링")
    void filterByThemeId() {
        List<ReservationResponse> response = sendRequestFilteringWithToken("?themeId=2");

        assertThat(response).containsExactlyInAnyOrder(
                ReservationResponse.from(PRE_INSERTED_RESERVATION_1),
                ReservationResponse.from(PRE_INSERTED_RESERVATION_3)
        );
    }

    @Test
    @DisplayName("시작 날짜로 필터링")
    void filterByDateFrom() {
        List<ReservationResponse> response = sendRequestFilteringWithToken("?dateFrom=2024-05-02");

        assertThat(response).containsExactlyInAnyOrder(
                ReservationResponse.from(PRE_INSERTED_RESERVATION_2),
                ReservationResponse.from(PRE_INSERTED_RESERVATION_4)
        );
    }

    @Test
    @DisplayName("끝 날짜로 필터링")
    void filterByTimeId() {
        List<ReservationResponse> response = sendRequestFilteringWithToken("?dateTo=2024-05-01");

        assertThat(response).containsExactlyInAnyOrder(
                ReservationResponse.from(PRE_INSERTED_RESERVATION_1),
                ReservationResponse.from(PRE_INSERTED_RESERVATION_3)
        );
    }

    @Test
    @DisplayName("종합 필터링")
    void filterComprehensive() {
        List<ReservationResponse> response = sendRequestFilteringWithToken("?memberId=2&themeId=3&dateFrom=2024-05-02&dateTo=2024-05-02");

        assertThat(response).containsExactlyInAnyOrder(
                ReservationResponse.from(PRE_INSERTED_RESERVATION_2)
        );
    }

    private List<ReservationResponse> sendRequestFilteringWithToken(String path) {
        TypeRef<List<ReservationResponse>> memberListFormat = new TypeRef<>() {
        };

        return RestAssured.given().log().all()
                .cookie("token", tokenFixture.adminToken)
                .when().get("/reservations/filter" + path)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(memberListFormat);
    }
}
