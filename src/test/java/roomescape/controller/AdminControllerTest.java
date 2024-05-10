package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Member;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;
import roomescape.domain.repository.MemberRepository;
import roomescape.domain.repository.ReservationTimeRepository;
import roomescape.domain.repository.ThemeRepository;
import roomescape.dto.request.AdminReservationRequest;
import roomescape.dto.request.LoginRequest;
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
        memberRepository.save(new Member("admin@naver.com", "admin", "어드민", Role.ADMIN));
        reservationTimeRepository.save(new ReservationTime(LocalTime.of(11, 0)));
        themeRepository.save(new Theme("테마 이름", "테마 설명", "https://example.com"));
    }

    @Test
    @DisplayName("예약을 생성한다.")
    void addReservation() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        AdminReservationRequest request = new AdminReservationRequest(1L, tomorrow, 1L, 1L);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getToken("admin@naver.com", "admin"))
                .body(request)
                .when().post("/admin/reservations")
                .then().log().all()
                .extract();

        ReservationResponse reservationResponse = response.as(ReservationResponse.class);
        MemberResponse memberResponse = new MemberResponse(1L, "admin@naver.com", "어드민", Role.ADMIN);
        ReservationTimeResponse reservationTimeResponse = new ReservationTimeResponse(1L, LocalTime.of(11, 0));
        ThemeResponse themeResponse = new ThemeResponse(1L, "테마 이름", "테마 설명", "https://example.com");

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            softly.assertThat(response.header("Location")).isEqualTo("/reservations/1");
            softly.assertThat(reservationResponse).isEqualTo(
                    new ReservationResponse(1L, memberResponse, tomorrow,
                            reservationTimeResponse,
                            themeResponse));
        });
    }

    @Test
    @DisplayName("예약들을 검색한다.")
    @Sql("/reservations.sql")
    void getAllReservations() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .cookie("token", getToken("admin@naver.com", "admin"))
                .params("memberId", 1)
                .params("themeId", "1")
                .params("dateFrom", "2024-04-01")
                .params("dateTo", "2024-04-07")
                .when().get("/admin/reservations/search")
                .then().log().all()
                .extract();

        List<ReservationResponse> reservationResponses = response.jsonPath().getList(".", ReservationResponse.class);
        MemberResponse memberResponse = new MemberResponse(1L, "qwer@naver.com", "구름", Role.NORMAL);
        ReservationTimeResponse reservationTimeResponse = new ReservationTimeResponse(3L, LocalTime.of(15, 0));
        ThemeResponse themeResponse = new ThemeResponse(1L, "테마1", "테마1 설명", "https://via.placeholder.com/150/92c952");

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(reservationResponses).hasSize(1);
            softly.assertThat(reservationResponses)
                    .containsExactly(new ReservationResponse(15L, memberResponse, LocalDate.of(2024, 4, 6),
                            reservationTimeResponse, themeResponse));
        });
    }

    private String getToken(String email, String password) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new LoginRequest(email, password))
                .when().post("/login")
                .then().log().all()
                .extract();

        return response.cookie("token");
    }
}
