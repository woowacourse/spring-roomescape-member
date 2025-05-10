package roomescape.reservation.presentation.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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
public class ThemeControllerTest {
    private ReservationFixture reservationFixture = new ReservationFixture();

    @Test
    @DisplayName("테마 추가 테스트")
    void createThemeTest() {
        // given
        ThemeRequest theme = reservationFixture.createTheme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        // when - then
        ApiHelper.post(ApiHelper.THEME_ENDPOINT, theme)
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("테마 전체 조회 테스트")
    void getThemesTest() {
        // given
        ThemeRequest theme = reservationFixture.createTheme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        ApiHelper.post(ApiHelper.THEME_ENDPOINT, theme);

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("테마 삭제 테스트")
    void deleteThemeTest() {
        // given
        ThemeRequest theme = reservationFixture.createTheme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        ApiHelper.post(ApiHelper.THEME_ENDPOINT, theme);

        // when - then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("예약이 이미 존재하는 테마는 삭제할 수 없다.")
    void deleteThemeExceptionTest() {
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
                .contentType(ContentType.JSON)
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(400);
    }
}
