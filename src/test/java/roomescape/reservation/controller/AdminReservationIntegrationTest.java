package roomescape.reservation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.controller.LoginController;
import roomescape.auth.service.AuthService;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.util.fixture.AuthFixture;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminReservationIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private ReservationRepository reservationRepository;

    @DisplayName("관리자 기능 요청 시 관리자 권한이 없으면 예외가 발생한다")
    @Test
    void admin_interceptor_test() {
        // given
        String token = AuthFixture.createUserToken(authService);

        Map<String, String> params = Map.of(
                "date", "2099-08-05",
                "timeId", "5",
                "themeId", "1",
                "memberId", "1"
        );

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(LoginController.TOKEN_COOKIE_NAME, token)
                .body(params)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(403)
                .body(equalTo("접근 권한이 없습니다."));
    }

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


    @DisplayName("조건에 해당하는 사용자 예약 목록을 반환한다")
    @MethodSource
    @ParameterizedTest
    void get_reservation_by_filter(
            Long memberId,
            Long themeId,
            LocalDate dateFrom,
            LocalDate dateTo,
            List<Long> expectedReservationIds
    ) {
        // given
        String token = AuthFixture.createAdminToken(authService);

        Map<String, String> queryParams = new HashMap<>();
        putIfNotNull(queryParams, "memberId", memberId);
        putIfNotNull(queryParams, "themeId", themeId);
        putIfNotNull(queryParams, "dateFrom", dateFrom);
        putIfNotNull(queryParams, "dateTo", dateTo);

        // when
        List<ReservationResponse> reservations = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(LoginController.TOKEN_COOKIE_NAME, token)
                .params(queryParams)
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", ReservationResponse.class);

        // then
        assertThat(reservations)
                .extracting(ReservationResponse::id)
                .containsExactlyInAnyOrderElementsOf(expectedReservationIds);
    }

    static Stream<Arguments> get_reservation_by_filter() {
        return Stream.of(
                Arguments.of(1L, null, null, null, List.of(1L, 2L, 3L, 8L, 11L)),
                Arguments.of(1L, 1L, null, null, List.of(1L, 2L, 3L)),
                Arguments.of(1L, null, LocalDate.now().minusDays(5), null, List.of(2L, 3L, 8L, 11L)),
                Arguments.of(1L, null, null, LocalDate.now().minusDays(1), List.of(1L, 2L, 3L, 8L)),
                Arguments.of(null, 2L, null, null, List.of(4L, 5L, 6L)),
                Arguments.of(null, 3L, LocalDate.now().minusDays(3), null, List.of(7L, 8L, 9L, 10L)),
                Arguments.of(null, 3L, null, LocalDate.now(), List.of(7L, 8L, 9L, 10L)),
                Arguments.of(null, null, LocalDate.now().minusDays(3), null,
                        List.of(6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L, 16L)),
                Arguments.of(null, null, null, LocalDate.now().minusDays(2), List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L)),
                Arguments.of(null, null, LocalDate.now().minusDays(3), LocalDate.now().minusDays(1),
                        List.of(6L, 7L, 8L, 9L)),
                Arguments.of(null, null, null, null,
                        List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L, 16L))
        );
    }

    private void putIfNotNull(Map<String, String> map, String key, Object value) {
        if (value != null) {
            map.put(key, value.toString());
        }
    }

}
