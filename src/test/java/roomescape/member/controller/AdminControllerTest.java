package roomescape.member.controller;

import static org.hamcrest.Matchers.is;
import static roomescape.fixture.MemberFixture.getMemberAdmin;
import static roomescape.fixture.MemberFixture.getMemberChoco;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.auth.controller.dto.SignUpRequest;
import roomescape.auth.service.TokenProvider;
import roomescape.member.controller.dto.MemberResponse;
import roomescape.member.service.MemberService;
import roomescape.reservation.controller.dto.MemberReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.controller.dto.ReservationTimeRequest;
import roomescape.reservation.controller.dto.ReservationTimeResponse;
import roomescape.reservation.controller.dto.ThemeRequest;
import roomescape.reservation.controller.dto.ThemeResponse;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.ReservationTimeService;
import roomescape.reservation.service.ThemeService;
import roomescape.util.ControllerTest;

@DisplayName("관리자 페이지 테스트")
class AdminControllerTest extends ControllerTest {
    @Autowired
    ReservationService reservationService;

    @Autowired
    ReservationTimeService reservationTimeService;

    @Autowired
    ThemeService themeService;

    @Autowired
    MemberService memberService;

    @Autowired
    TokenProvider tokenProvider;

    @DisplayName("관리자 메인 페이지 조회에 성공한다.")
    @Test
    void adminMainPage() {
        //given
        String token = tokenProvider.createAccessToken(getMemberAdmin().getEmail());

        //when & then
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("관리자 예약 페이지 조회에 성공한다.")
    @Test
    void adminReservationPage() {
        //given
        String token = tokenProvider.createAccessToken(getMemberAdmin().getEmail());

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @DisplayName("관리자 예약 생성 시 201을 반환한다.")
    @Test
    void create() {
        //given
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.create(
                new ReservationTimeRequest("12:00"));
        ThemeResponse themeResponse = themeService.create(new ThemeRequest("name", "description", "thumbnail"));
        MemberResponse memberResponse = memberService.create(
                new SignUpRequest(getMemberChoco().getName(), getMemberChoco().getEmail(), "1234"));

        String token = tokenProvider.createAccessToken(getMemberAdmin().getEmail());

        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberResponse.id());
        params.put("date", "2099-08-05");
        params.put("timeId", reservationTimeResponse.id());
        params.put("themeId", themeResponse.id());

        //when & then
        RestAssured.given().log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("관리자 예약 삭제 시 204를 반환한다.")
    @Test
    void delete() {
        //given
        long memberId = 1L;
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.create(
                new ReservationTimeRequest("12:00"));
        ThemeResponse themeResponse = themeService.create(new ThemeRequest("name", "description", "thumbnail"));

        String token = tokenProvider.createAccessToken(getMemberAdmin().getEmail());

        ReservationResponse reservationResponse = reservationService.createMemberReservation(
                new MemberReservationRequest(
                        memberId,
                        LocalDate.now().toString(),
                        reservationTimeResponse.id(),
                        themeResponse.id()
                )
        );

        //when &then
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().delete("/admin/reservations/" + reservationResponse.memberReservationId())
                .then().log().all()
                .statusCode(204);
    }
}
