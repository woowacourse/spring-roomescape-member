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
        // 어드민 계정, 예약 시간, 테마 데이터 생성
        memberRepository.save(new Member("admin@naver.com", "admin", "어드민", Role.ADMIN));
        reservationTimeRepository.save(new ReservationTime(LocalTime.of(11, 0)));
        themeRepository.save(new Theme("테마 이름", "테마 설명", "https://example.com"));
    }

    @Test
    @DisplayName("관리자 권한으로 사용자의 id를 받아 예약을 생성한다.")
    void addReservation() {

        // 어드민 예약 생성 요청
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        AdminReservationRequest request = new AdminReservationRequest(1L, tomorrow, 1L, 1L);

        // 어드민으로 로그인하여 예약 생성
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getToken("admin@naver.com", "admin"))
                .body(request)
                .when().post("/admin/reservations")
                .then().log().all()
                .extract();

        // 검증: 응답 상태 코드는 CREATED, 예약 정보는 예상과 일치
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            softly.assertThat(response.header("Location")).isEqualTo("/reservations/1");
            softly.assertThat(response.as(ReservationResponse.class)).isEqualTo(
                    new ReservationResponse(
                            1L,
                            new MemberResponse(1L, "admin@naver.com", "어드민", Role.ADMIN), tomorrow,
                            new ReservationTimeResponse(1L, LocalTime.of(11, 0)),
                            new ThemeResponse(1L, "테마 이름", "테마 설명", "https://example.com")));
        });
    }

    @Test
    @DisplayName("예약자와 테마와 날짜 기간 조건을 사용하여 예약들을 검색한다.")
    @Sql("/reservations.sql")
    void getAllReservations() {
        // 2024-04-01부터 2024-04-07까지 구름이 예약한 테마1 데이터를 검색
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .cookie("token", getToken("admin@naver.com", "admin"))
                .params("memberId", 1)
                .params("themeId", "1")
                .params("dateFrom", "2024-04-01")
                .params("dateTo", "2024-04-07")
                .when().get("/admin/reservations/search")
                .then().log().all()
                .extract();

        // 검색된 예약 데이터
        List<ReservationResponse> reservationResponses = response.jsonPath().getList(".", ReservationResponse.class);

        // 검증: 응답 상태 코드는 OK, 예약은 1개, 예약 정보는 예상과 일치
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(reservationResponses).hasSize(1);
            softly.assertThat(reservationResponses).containsExactly(new ReservationResponse(
                    15L,
                    new MemberResponse(1L, "qwer@naver.com", "구름", Role.NORMAL), LocalDate.of(2024, 4, 6),
                    new ReservationTimeResponse(3L, LocalTime.of(15, 0)),
                    new ThemeResponse(1L, "테마1", "테마1 설명", "https://via.placeholder.com/150/92c952"))
            );
        });
    }

    private String getToken(String email, String password) {
        // 로그인 요청
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new LoginRequest(email, password))
                .when().post("/login")
                .then().log().all()
                .extract();

        // 토큰 반환
        return response.cookie("token");
    }
}
