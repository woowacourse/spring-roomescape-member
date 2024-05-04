package roomescape.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationSaveRequest;
import roomescape.exception.ErrorResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static roomescape.TestFixture.*;

class ReservationAcceptanceTest extends ApiAcceptanceTest {

    @Test
    @DisplayName("[Step2, Step5] 예약 목록을 조회한다.")
    void findReservations() {
        // given & when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .extract();
        final List<ReservationResponse> reservationResponses
                = Arrays.stream(response.as(ReservationResponse[].class)).toList();

        // then
        assertAll(() -> {
            checkHttpStatusOk(response);
            assertThat(reservationResponses).hasSize(0);
        });
    }

    @Test
    @DisplayName("[Step3, Step6, Step8] 예약을 추가한다.")
    void createOneReservation() {
        // given & when
        final Long themeId = saveTheme(WOOTECO_THEME_NAME, WOOTECO_THEME_DESCRIPTION);
        final Long timeId = saveReservationTime(MIA_RESERVATION_TIME);

        final ReservationSaveRequest request = new ReservationSaveRequest(USER_MIA, MIA_RESERVATION_DATE, timeId, themeId);
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .extract();
        final ReservationResponse reservationResponse = response.as(ReservationResponse.class);

        // then
        assertAll(() -> {
            checkHttpStatusCreated(response);
            assertThat(reservationResponse.id()).isNotNull();
            assertThat(reservationResponse.name()).isEqualTo(USER_MIA);
        });
    }

    @Test
    @DisplayName("[Step3, Step6, Step8] 잘못된 형식의 예약을 추가한다.")
    void createInvalidReservation() {
        // given & when
        final Long themeId = saveTheme(WOOTECO_THEME_NAME, WOOTECO_THEME_DESCRIPTION);
        final Long timeId = saveReservationTime(MIA_RESERVATION_TIME);

        final ReservationSaveRequest request = new ReservationSaveRequest(null, MIA_RESERVATION_DATE, timeId, themeId);
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .extract();
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        // then
        assertAll(() -> {
            checkHttpStatusBadRequest(response);
            assertThat(errorResponse.message()).isNotNull();
        });
    }

    @Test
    @DisplayName("[Step3, Step6, Step8] 존재하지 않는 예약 시간에 예약을 추가한다.")
    void createReservationWithNotExistingTime() {
        // given & when
        final Long notExistingTimeId = 1L;
        final Long themeId = saveTheme(WOOTECO_THEME_NAME, WOOTECO_THEME_DESCRIPTION);

        final ReservationSaveRequest request = new ReservationSaveRequest(USER_MIA, MIA_RESERVATION_DATE, notExistingTimeId, themeId);
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .extract();
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        // then
        assertAll(() -> {
            checkHttpStatusNotFound(response);
            assertThat(errorResponse.message()).isNotNull();
        });
    }

    @TestFactory
    @DisplayName("[Step3, Step6, Step8] 예약을 추가하고 삭제한다.")
    Stream<DynamicTest> createThenDeleteReservation() {
        return Stream.of(
                dynamicTest("예약을 하나 생성한다.", this::createOneReservation),
                dynamicTest("예약이 하나 생성된 예약 목록을 조회한다.", () -> findReservationsWithSize(1)),
                dynamicTest("예약을 하나 삭제한다.", this::deleteOneReservation),
                dynamicTest("예약 목록을 조회한다.", () -> findReservationsWithSize(0))
        );
    }

    void deleteOneReservation() {
        // given & when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .extract();

        // then
        checkHttpStatusNoContent(response);
    }

    void findReservationsWithSize(final int size) {
        // given & when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .extract();
        final List<ReservationResponse> reservationResponses = Arrays.stream(response.as(ReservationResponse[].class))
                .toList();

        // then
        assertAll(() -> {
            checkHttpStatusOk(response);
            assertThat(reservationResponses).hasSize(size)
                    .extracting(ReservationResponse::id)
                    .isNotNull();
        });
    }

    @Test
    @DisplayName("[Step3, Step6, Step8] 존재하지 않는 예약을 삭제한다.")
    void deleteNotExistingReservation() {
        // given & when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .extract();
        final ErrorResponse errorResponse = response.as(ErrorResponse.class);

        // then
        assertAll(() -> {
            checkHttpStatusNotFound(response);
            assertThat(errorResponse.message()).isNotNull();
        });
    }
}
