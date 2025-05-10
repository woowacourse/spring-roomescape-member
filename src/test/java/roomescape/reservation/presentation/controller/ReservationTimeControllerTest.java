package roomescape.reservation.presentation.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.List;
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
public class ReservationTimeControllerTest {
    private ReservationFixture reservationFixture = new ReservationFixture();

    @Test
    @DisplayName("시간 추가 테스트")
    void createTimeTest() {
        // given
        ReservationTimeRequest reservationTime = reservationFixture.createReservationTime("10:00");

        // when-then
        ApiHelper.post(ApiHelper.TIME_ENDPOINT, reservationTime)
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("시작 시간은 LocalTime 형식을 만족시켜야 한다.")
    void createTimeExceptionTest() {
        // given
        Map<String, String> reservationTime = new HashMap<>();
        reservationTime.put("startAt", "10-10");

        // when - then
        ApiHelper.post(ApiHelper.TIME_ENDPOINT, reservationTime)
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("중복된 시간 추가는 불가능하다.")
    void createTimeDuplicateExceptionTest() {
        // given
        ReservationTimeRequest reservationTime = reservationFixture.createReservationTime("10:00");
        ApiHelper.post(ApiHelper.TIME_ENDPOINT, reservationTime);

        // when - then
        ApiHelper.post(ApiHelper.TIME_ENDPOINT, reservationTime)
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("시간 조회 테스트")
    void getTimesTest() {
        // given
        ReservationTimeRequest reservationTime = reservationFixture.createReservationTime("10:00");
        ApiHelper.post(ApiHelper.TIME_ENDPOINT, reservationTime);

        // when-then
        RestAssured.given().log().all()
                .when().get(ApiHelper.TIME_ENDPOINT)
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("예약 가능 시간 조회 테스트")
    void getAvailableTimesTest() {
        // given
        String token = ApiHelper.getUserToken();

        ReservationTimeRequest availableReservationTime = reservationFixture.createReservationTime("10:00");
        ReservationTimeRequest unAvailableReservationTime = reservationFixture.createReservationTime("11:00");
        ApiHelper.post(ApiHelper.TIME_ENDPOINT, availableReservationTime);
        ApiHelper.post(ApiHelper.TIME_ENDPOINT, unAvailableReservationTime);

        ThemeRequest theme = reservationFixture.createTheme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        ApiHelper.post(ApiHelper.THEME_ENDPOINT, theme);

        MemberReservationRequest reservation = reservationFixture.createMemberReservation("2025-08-05", "1", "2");
        ApiHelper.postWithToken(ApiHelper.RESERVATION_ENDPOINT, reservation, token);

        // when
        RestAssured.given().log().all()
                .when().get("/times/available?date=2025-08-05&themeId=1")
                .then().log().all()
                .statusCode(200)
                .body("alreadyBooked", is(List.of(false, true)));
    }

    @Test
    @DisplayName("시간 삭제 테스트")
    void deleteTimeTest() {
        // given
        ReservationTimeRequest reservationTime = reservationFixture.createReservationTime("10:00");
        ApiHelper.post(ApiHelper.TIME_ENDPOINT, reservationTime);

        // when-then
        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("예약이 이미 존재하는 시간은 삭제할 수 없다.")
    void deleteTimeExceptionTest() {
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
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(400);
    }
}
