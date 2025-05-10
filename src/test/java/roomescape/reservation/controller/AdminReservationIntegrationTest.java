package roomescape.reservation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.controller.LoginController;
import roomescape.auth.service.AuthService;
import roomescape.global.config.TestConfig;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.util.fixture.AuthFixture;

@Import(TestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminReservationIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private ReservationRepository reservationRepository;

    @DisplayName("관리자 API 경로로 예약을 생성한다")
    @Test
    void add_reservation_test() {
        //given
        String token = AuthFixture.createAdminToken(authService);

        Map<String, String> params = Map.of(
                "date", "2099-08-05",
                "timeId", "5",
                "themeId", "1",
                "memberId", "1"
        );

        // when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(LoginController.TOKEN_COOKIE_NAME, token)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(17));

        // then
        Reservation savedReservation = reservationRepository.findById(17L).get();

        assertAll(
                () -> assertThat(savedReservation.getId()).isEqualTo(17L),
                () -> assertThat(savedReservation.getDate()).isEqualTo(LocalDate.of(2099, 8, 5)),
                () -> assertThat(savedReservation.getTimeId()).isEqualTo(5L),
                () -> assertThat(savedReservation.getThemeId()).isEqualTo(1L)
        );
    }

}
