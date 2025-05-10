package roomescape.admin.presentation.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.admin.presentation.dto.AdminReservationRequest;
import roomescape.admin.presentation.fixture.AdminFixture;
import roomescape.global.ApiHelper;
import roomescape.reservation.presentation.dto.ReservationTimeRequest;
import roomescape.reservation.presentation.dto.ThemeRequest;
import roomescape.reservation.presentation.fixture.ReservationFixture;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminControllerTest {
    private final AdminFixture adminFixture = new AdminFixture();
    private final ReservationFixture reservationFixture = new ReservationFixture();

    @Test
    @DisplayName("예약 추가 테스트")
    void createReservationTest() {
        // given
        String token = ApiHelper.getAdminToken();

        ReservationTimeRequest reservationTime = reservationFixture.createReservationTime("10:00");
        ApiHelper.post(ApiHelper.TIME_ENDPOINT, reservationTime);

        ThemeRequest theme = reservationFixture.createTheme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        ApiHelper.post(ApiHelper.THEME_ENDPOINT, theme);

        AdminReservationRequest reservation = adminFixture.createAdminReservation("2025-08-05", "1", "1", "1");

        // when - then
        ApiHelper.postWithToken("/admin/reservations", reservation, token)
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("예약 목록 검색 테스트")
    void getFilteredReservationsTest() {
        // given
        String token = ApiHelper.getAdminToken();

        ReservationTimeRequest reservationTime = reservationFixture.createReservationTime("10:00");
        ApiHelper.post(ApiHelper.TIME_ENDPOINT, reservationTime);

        ThemeRequest theme = reservationFixture.createTheme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        ApiHelper.post(ApiHelper.THEME_ENDPOINT, theme);

        AdminReservationRequest reservation1 = adminFixture.createAdminReservation("2025-08-05", "1", "1", "1");
        ApiHelper.postWithToken("/admin/reservations", reservation1, token);
        AdminReservationRequest reservation2 = adminFixture.createAdminReservation("2025-08-10", "1", "1", "1");
        ApiHelper.postWithToken("/admin/reservations", reservation2, token);
        AdminReservationRequest reservation3 = adminFixture.createAdminReservation("2025-10-05", "1", "1", "1");
        ApiHelper.postWithToken("/admin/reservations", reservation3, token);

        // when - then
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/reservations?dateFrom=2025-08-01&dateTo=2025-09-01")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }
}
