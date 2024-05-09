package roomescape.controller.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import roomescape.controller.BaseControllerTest;
import roomescape.domain.member.Role;
import roomescape.dto.request.AdminReservationRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.ThemeResponse;

@Sql("/integration-data.sql")
class AdminControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("어드민이 예약을 추가한다.")
    void addAdminReservation() {
        doReturn(ADMIN_ID).when(jwtTokenProvider).getMemberId(any());

        AdminReservationRequest request = new AdminReservationRequest(
                LocalDate.of(2024, 6, 22),
                1L,
                1L,
                4L
        );

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .cookie("token", "mock-token")
                .when().post("/admin/reservations")
                .then().log().all()
                .extract();

        ReservationResponse reservationResponse = response.as(ReservationResponse.class);
        MemberResponse memberResponse = reservationResponse.member();
        ReservationTimeResponse timeResponse = reservationResponse.time();
        ThemeResponse themeResponse = reservationResponse.theme();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            softly.assertThat(reservationResponse.date()).isEqualTo(LocalDate.of(2024, 6, 22));
            softly.assertThat(memberResponse)
                    .isEqualTo(new MemberResponse(4L, "alstn3@gmail.com", "구름3", Role.USER));
            softly.assertThat(timeResponse).isEqualTo(new ReservationTimeResponse(1L, LocalTime.of(9, 0)));
            softly.assertThat(themeResponse).isEqualTo(new ThemeResponse(1L, "고풍 한옥 마을", "한국의 전통적인 아름다움이 당신을 맞이합니다.",
                    "https://via.placeholder.com/150/92c952"));
        });
    }
}
