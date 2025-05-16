package roomescape.reservation.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.controller.LoginController;
import roomescape.auth.service.AuthService;
import roomescape.reservation.controller.dto.AvailableTimeResponse;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.util.fixture.AuthFixture;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private ReservationRepository reservationRepository;

    @DisplayName("예약 목록의 조회 시 DB에 저장된 예약 목록을 반환한다")
    @Test
    void get_reservations_test() {
        // when
        List<ReservationResponse> reservations = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationResponse.class);

        // then
        List<Reservation> savedReservations = reservationRepository.findAll();
        assertThat(reservations)
                .extracting(ReservationResponse::date)
                .containsExactlyInAnyOrderElementsOf(
                        savedReservations.stream()
                                .map(Reservation::getDate)
                                .toList()
                );
    }

    @DisplayName("예약 생성 시 사용자의 요청 쿠키 내부에 토큰이 존재하지 않으면 예외가 발생한다")
    @Test
    void extract_token_exception_test() {
        // when & then
        RestAssured.given().log().all()
                .cookie("empty", "")
                .when().post("/reservations")
                .then().log().all()
                .statusCode(401)
                .body(equalTo("인증에 실패했습니다."));
    }

    @DisplayName("예약을 생성하면 DB에 예약 데이터가 저장된다")
    @Test
    void add_reservation_test() {
        //given
        String token = AuthFixture.createUserToken(authService);

        Map<String, String> params = Map.of(
                "date", "2026-08-05",
                "timeId", "6",
                "themeId", "2"
        );

        // when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(LoginController.TOKEN_COOKIE_NAME, token)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(17));

        // then
        Reservation savedReservation = reservationRepository.findById(17L).get();

        assertAll(
                () -> assertThat(savedReservation.getId()).isEqualTo(17L),
                () -> assertThat(savedReservation.getDate()).isEqualTo(LocalDate.of(2026, 8, 5)),
                () -> assertThat(savedReservation.getTimeId()).isEqualTo(6L),
                () -> assertThat(savedReservation.getThemeId()).isEqualTo(2L)
        );
    }

    @DisplayName("예약을 삭제하면 DB의 예약 데이터가 삭제된다")
    @Test
    void delete_reservation_test() {
        // given
        String userToken = AuthFixture.createUserToken(authService);

        // when
        RestAssured.given().log().all()
                .cookie(LoginController.TOKEN_COOKIE_NAME, userToken)
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        // then
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(15));

        assertThat(reservationRepository.findById(1L).isEmpty()).isTrue();
    }

    @DisplayName("특정 날짜와 테마에 대해 이용가능한 시간 목록을 반환한다")
    @Test
    void get_available_times_test() {
        // when
        List<AvailableTimeResponse> availableTimes = RestAssured.given().log().all()
                .queryParam("date", LocalDate.now().minusDays(5).toString())
                .queryParam("themeId", "1")
                .when().get("/reservations/available")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", AvailableTimeResponse.class);

        // then
        List<Long> bookedTimeIds = availableTimes.stream()
                .filter(AvailableTimeResponse::alreadyBooked)
                .map(AvailableTimeResponse::timeId)
                .toList();

        assertThat(bookedTimeIds).containsExactly(2L, 3L);
    }

    @DisplayName("예약 생성 시 요청 날짜가 형식에 맞지 않으면 400에러가 발생한다")
    @ValueSource(strings = {"2025-13-33", "2025:11:21", "20251225"})
    @ParameterizedTest
    void add_reservation_date_format_exception(String inputDateString) {
        // given
        String token = AuthFixture.createUserToken(authService);

        Map<String, String> requestBody = Map.of(
                "date", inputDateString,
                "timeId", "1",
                "themeId", "1",
                "memberId", "2"
        );

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(LoginController.TOKEN_COOKIE_NAME, token)
                .body(requestBody)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body(equalTo("요청 형식이 올바르지 않습니다."));

    }

    @DisplayName("예약 생성 시 존재하지 않는 예약 시간 ID를 입력하면 예외가 발생한다")
    @Test
    void add_reservation_time_id_exception() {
        // given
        String token = AuthFixture.createUserToken(authService);

        Map<String, String> requestBody = Map.of(
                "date", "2025-05-06",
                "timeId", "200",
                "themeId", "1",
                "memberId", "3"
        );

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(LoginController.TOKEN_COOKIE_NAME, token)
                .body(requestBody)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404)
                .body(equalTo("해당하는 시간 정보가 존재하지 않습니다."));
    }

    @DisplayName("예약 생성 시 존재하지 않는 테마 ID를 입력하면 예외가 발생한다")
    @Test
    void add_reservation_theme_id_exception() {
        // given
        String token = AuthFixture.createUserToken(authService);

        Map<String, String> requestBody = Map.of(
                "date", LocalDate.now().plusDays(3).toString(),
                "timeId", "1",
                "themeId", "200",
                "memberId", "1"
        );

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(LoginController.TOKEN_COOKIE_NAME, token)
                .body(requestBody)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404)
                .body(equalTo("해당하는 테마가 존재하지 않습니다."));
    }

    @DisplayName("예약 생성 시 지난 날짜 혹은 지난 시각을 입력하면 예외가 발생한다")
    @MethodSource
    @ParameterizedTest
    void add_reservation_past_exception(Map<String, String> requestBody, String errorMessage) {
        // given
        String token = AuthFixture.createUserToken(authService);

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(LoginController.TOKEN_COOKIE_NAME, token)
                .body(requestBody)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body(equalTo(errorMessage));
    }


    static Stream<Arguments> add_reservation_null_empty_exception() {
        String saveDate = LocalDate.now().plusDays(2).toString();
        return Stream.of(
                Arguments.of(
                        Map.of(
                                "date", saveDate,
                                "timeId", "1",
                                "themeId", "1"
                        )
                ),
                Arguments.of(
                        Map.of(
                                "date", saveDate,
                                "timeId", "1",
                                "themeId", "1"
                        )
                ),
                Arguments.of(
                        Map.of(
                                "timeId", "1",
                                "themeId", "1"
                        )
                ),
                Arguments.of(
                        Map.of(
                                "date", saveDate,
                                "themeId", "1"
                        )
                ),
                Arguments.of(
                        Map.of(
                                "date", saveDate,
                                "timeId", "1"
                        )
                )
        );
    }

    static Stream<Arguments> add_reservation_past_exception() {
        return Stream.of(
                Arguments.of(
                        Map.of(
                                "date", LocalDate.now().minusDays(10).toString(),
                                "timeId", "1",
                                "themeId", "1"
                        ),
                        "지난 날짜는 예약할 수 없습니다."
                ),
                Arguments.of(
                        Map.of(
                                "date", LocalDate.now().toString(),
                                "timeId", "1",
                                "themeId", "1"
                        ),
                        "지난 시각은 예약할 수 없습니다."
                )
        );
    }

}
