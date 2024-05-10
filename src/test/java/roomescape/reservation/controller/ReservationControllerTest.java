package roomescape.reservation.controller;

import static org.hamcrest.Matchers.containsString;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.domain.repository.MemberRepository;
import roomescape.member.dto.LoginMember;
import roomescape.member.dto.LoginRequest;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.repository.ReservationTimeRepository;
import roomescape.reservation.domain.repository.ThemeRepository;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.service.ReservationService;
import roomescape.util.ControllerTest;

@DisplayName("예약 API 통합 테스트")
class ReservationControllerTest extends ControllerTest {
    @Autowired
    ReservationTimeRepository reservationTimeRepository;
    @Autowired
    ThemeRepository themeRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ReservationService reservationService;

    Member member;
    Theme theme;
    ReservationTime reservationTime;

    @BeforeEach
    void setData() {
        reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.MIDNIGHT));
        theme = themeRepository.save(new Theme("name", "description", "thumbnail"));
        member = memberRepository.save(new Member("name", "email@email.com", "Password", Role.MEMBER));
    }

    @DisplayName("인기 테마 페이지 조회에 성공한다.")
    @Test
    void popularPage() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(200)
                .body(containsString("인기 테마"));
    }

    @DisplayName("예약 조회 시 200을 반환한다.")
    @Test
    void find() {
        //given
        ReservationRequest reservationRequest = new ReservationRequest("2099-04-18", reservationTime.getId(),
                theme.getId());
        ReservationResponse reservationResponse = reservationService.create(reservationRequest,
                new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole()));

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("예약 생성 시 201을 반환한다.")
    @Test
    void create() {
        //given
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("date", "2099-08-05");
        reservation.put("timeId", reservationTime.getId());
        reservation.put("themeId", theme.getId());

        String accessToken = RestAssured
                .given().log().all()
                .body(new LoginRequest(member.getEmail(), member.getPassword()))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract().cookie("token");

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", accessToken)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("예약 생성 시, 잘못된 날짜 형식에 대해 400을 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "20-12-31", "2020-1-30", "2020-11-0", "-1"})
    void createBadRequest(String date) {
        //given
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("date", date);
        reservation.put("timeId", reservationTime.getId());
        reservation.put("themeId", theme.getId());

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("예약 생성 시, 빈 시간 id에 대해 400을 반환한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createTimeIdBadRequest(String timeId) {
        //given
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("date", "2099-12-01");
        reservation.put("timeId", timeId);
        reservation.put("themeId", theme.getId());

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("예약 생성 시, 빈 테마 id에 대해 400을 반환한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createThemeIdBadRequest(String themeId) {
        //given
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("date", "2099-12-01");
        reservation.put("timeId", reservationTime.getId());
        reservation.put("themeId", themeId);

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("예약 삭제 시 204를 반환한다.")
    @Test
    void delete() {
        //given
        ReservationRequest reservationRequest = new ReservationRequest("2099-04-18", reservationTime.getId(),
                theme.getId());
        ReservationResponse reservationResponse = reservationService.create(reservationRequest,
                new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole()));
        long id = reservationResponse.id();

        //when &then
        RestAssured.given().log().all()
                .when().delete("/reservations/" + id)
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("존재하지 않은 예약 삭제 시 400를 반환한다.")
    @Test
    void reservationNotFound() {
        //given & when & then
        RestAssured.given().log().all()
                .when().delete("/reservations/6")
                .then().log().all()
                .statusCode(400);
    }
}
