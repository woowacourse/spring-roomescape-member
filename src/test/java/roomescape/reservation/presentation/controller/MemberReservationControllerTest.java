package roomescape.reservation.presentation.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.global.ApiHelper;
import roomescape.reservation.presentation.dto.MemberReservationRequest;
import roomescape.reservation.presentation.dto.ReservationTimeRequest;
import roomescape.reservation.presentation.dto.ThemeRequest;
import roomescape.reservation.presentation.fixture.ReservationFixture;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MemberReservationControllerTest {
    ReservationFixture reservationFixture = new ReservationFixture();

    @Test
    @DisplayName("예약 추가 테스트")
    void createReservationTest() {
        // given
        String token = ApiHelper.getUserToken();

        ReservationTimeRequest reservationTime = reservationFixture.createReservationTime("10:00");
        ApiHelper.post(ApiHelper.TIME_ENDPOINT, reservationTime);

        ThemeRequest theme = reservationFixture.createTheme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        ApiHelper.post(ApiHelper.THEME_ENDPOINT, theme);

        MemberReservationRequest reservation = reservationFixture.createMemberReservation("2025-08-05", "1", "1");

        // when - then
        ApiHelper.postWithToken(ApiHelper.RESERVATION_ENDPOINT, reservation, token)
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("날짜는 LocalDate 형식을 만족시켜야 한다.")
    void createReservationDateExceptionTest() {
        // given
        String token = ApiHelper.getUserToken();

        Map<String, String> reservation = new HashMap<>();
        reservation.put("date", "2025:08:05");
        reservation.put("themeId", "1");
        reservation.put("timeId", "1");

        // when - then
        ApiHelper.postWithToken(ApiHelper.RESERVATION_ENDPOINT, reservation, token)
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("지나간 날짜와 시간에 대한 예약 생성은 불가능하다.")
    void createReservationIsPastDateExceptionTest() {
        // given
        String token = ApiHelper.getUserToken();

        ReservationTimeRequest reservationTime = reservationFixture.createReservationTime("10:00");
        ApiHelper.post(ApiHelper.TIME_ENDPOINT, reservationTime);

        MemberReservationRequest reservation = reservationFixture.createMemberReservation("2024-08-05", "1", "1");

        // when - then
        ApiHelper.postWithToken(ApiHelper.RESERVATION_ENDPOINT, reservation, token)
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("중복된 일시의 예약은 불가능하다.")
    void createReservationIsDuplicateDateExceptionTest() {
        // given
        String token = ApiHelper.getUserToken();

        ReservationTimeRequest reservationTime = reservationFixture.createReservationTime("10:00");
        ApiHelper.post(ApiHelper.TIME_ENDPOINT, reservationTime);

        ThemeRequest theme = reservationFixture.createTheme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        ApiHelper.post(ApiHelper.THEME_ENDPOINT, theme);

        MemberReservationRequest reservation = reservationFixture.createMemberReservation("2025-08-05", "1", "1");
        ApiHelper.postWithToken(ApiHelper.RESERVATION_ENDPOINT, reservation, token);

        // when - then
        ApiHelper.postWithToken(ApiHelper.RESERVATION_ENDPOINT, reservation, token)
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간 ID를 이용하여 예약할 수 없다.")
    void createReservationInvalidTimeIdExceptionTest() {
        // given
        String token = ApiHelper.getUserToken();

        ThemeRequest theme = reservationFixture.createTheme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        ApiHelper.post(ApiHelper.THEME_ENDPOINT, theme);

        MemberReservationRequest reservation = reservationFixture.createMemberReservation("2025-08-05", "1", "1");
        // when - then
        ApiHelper.postWithToken(ApiHelper.RESERVATION_ENDPOINT, reservation, token)
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("존재하지 않는 테마 ID를 이용하여 예약할 수 없다.")
    void createReservationInvalidThemeIdExceptionTest() {
        // given
        String token = ApiHelper.getUserToken();

        ReservationTimeRequest reservationTime = reservationFixture.createReservationTime("10:00");
        ApiHelper.post(ApiHelper.TIME_ENDPOINT, reservationTime);

        MemberReservationRequest reservation = reservationFixture.createMemberReservation("2025-08-05", "1", "1");
        // when - then
        ApiHelper.postWithToken(ApiHelper.RESERVATION_ENDPOINT, reservation, token)
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 삭제 테스트")
    void deleteReservationTest() {
        // given
        String token = ApiHelper.getUserToken();

        ReservationTimeRequest reservationTime = reservationFixture.createReservationTime("10:00");
        ApiHelper.post(ApiHelper.TIME_ENDPOINT, reservationTime);

        ThemeRequest theme = reservationFixture.createTheme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        ApiHelper.post(ApiHelper.THEME_ENDPOINT, theme);

        MemberReservationRequest reservation = reservationFixture.createMemberReservation("2025-08-05", "1", "1");
        ApiHelper.postWithToken(ApiHelper.RESERVATION_ENDPOINT, reservation, token);

        // when - then
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("예약 조회 테스트")
    void reservationPageTest() {
        // given
        String token = ApiHelper.getUserToken();

        ReservationTimeRequest reservationTime = reservationFixture.createReservationTime("10:00");
        ApiHelper.post(ApiHelper.TIME_ENDPOINT, reservationTime);

        ThemeRequest theme = reservationFixture.createTheme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        ApiHelper.post(ApiHelper.THEME_ENDPOINT, theme);

        MemberReservationRequest reservation = reservationFixture.createMemberReservation("2025-08-05", "1", "1");
        ApiHelper.postWithToken(ApiHelper.RESERVATION_ENDPOINT, reservation, token);

        // when-then
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

}
