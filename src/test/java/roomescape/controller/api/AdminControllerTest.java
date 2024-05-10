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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import roomescape.controller.BaseControllerTest;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;
import roomescape.domain.member.Role;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.ReservationTimeRepository;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeRepository;
import roomescape.dto.request.AdminReservationRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.ThemeResponse;

class AdminControllerTest extends BaseControllerTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        memberRepository.save(new Member("admin@gmail.com", "password", "어드민", Role.ADMIN));
        memberRepository.save(new Member("user@gmail.com", "password", "유저", Role.USER));
        reservationTimeRepository.save(new ReservationTime(LocalTime.of(11, 0)));
        themeRepository.save(new Theme("테마 이름", "테마 설명", "https://example.com"));
    }

    @Test
    @DisplayName("어드민이 예약을 추가한다.")
    void addAdminReservation() {
        doReturn(1L).when(jwtTokenProvider).getMemberId(any());

        AdminReservationRequest request = new AdminReservationRequest(
                LocalDate.of(2024, 6, 22),
                1L,
                1L,
                1L
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
                    .isEqualTo(new MemberResponse(1L, "admin@gmail.com", "어드민", Role.ADMIN));
            softly.assertThat(timeResponse).isEqualTo(new ReservationTimeResponse(1L, LocalTime.of(11, 0)));
            softly.assertThat(themeResponse).isEqualTo(new ThemeResponse(1L, "테마 이름", "테마 설명",
                    "https://example.com"));
        });
    }

    @Test
    @DisplayName("어드민이 아니면 예약을 추가할 수 없다.")
    void addAdminReservationFailWhenNotAdmin() {
        doReturn(2L).when(jwtTokenProvider).getMemberId(any());

        AdminReservationRequest request = new AdminReservationRequest(
                LocalDate.of(2024, 6, 22),
                1L,
                1L,
                1L
        );

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .cookie("token", "mock-token")
                .when().post("/admin/reservations")
                .then().log().all()
                .extract();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
        });
    }
}
