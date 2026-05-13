package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.vo.ThemeImageUrl;
import roomescape.domain.vo.ThemeName;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.exception.ErrorCode;
import roomescape.exception.ErrorMessageResponse;
import roomescape.exception.RoomEscapeException;
import roomescape.service.ReservationService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationControllerTest {

    private static final ReservationTime TIME = new ReservationTime(1L, "12:00");
    private static final Theme THEME = new Theme(1L, new ThemeName("name"), "description",
        ThemeImageUrl.defaultImageUrl());

    @LocalServerPort
    private int port;

    @MockitoBean
    private ReservationService reservationService;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @Test
    void 예약을_추가한다() {
        // given
        Reservation reservation = reservation();
        ReservationRequest request = requestDtoFrom(
            reservation
        );

        when(reservationService.addReservation(any()))
            .thenReturn(reservation.withId(1L));

        // when
        Response response = RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/reservations");

        // then
        response
            .then()
            .statusCode(HttpStatus.CREATED.value());

        ReservationResponse responseDto = response.as(ReservationResponse.class);
        assertThat(responseDto.id()).isEqualTo(1L);

        verify(reservationService, times(1)).addReservation(any());
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 중복_예약이_존재하는_경우_예외_응답을_반환한다() {
        // given
        RoomEscapeException exception = new RoomEscapeException(ErrorCode.DUPLICATED_RESERVATION);
        Reservation reservation = reservation();
        ReservationRequest request = requestDtoFrom(reservation);
        when(reservationService.addReservation(any()))
            .thenThrow(exception);

        // when
        Response response = RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/reservations");

        // then
        response
            .then()
            .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());

        ErrorMessageResponse actualResponse = response.as(ErrorMessageResponse.class);
        ErrorMessageResponse expectedResponse = ErrorMessageResponse.of(exception.getCode());
        assertThat(actualResponse).isEqualTo(expectedResponse);

        verify(reservationService, times(1)).addReservation(any());
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 모든_예약을_조회한다() {
        // given
        Reservation reservation = reservation();
        List<Reservation> reservations = List.of(
            reservation.withId(1L), reservation.withId(2L), reservation.withId(3L));

        List<ReservationResponse> expectedResponse = reservations.stream()
            .map(ReservationResponse::from)
            .toList();

        when(reservationService.getReservations())
            .thenReturn(reservations);

        // when
        Response response = RestAssured
            .given().log().all()
            .when().get("/reservations");

        // then
        response
            .then()
            .statusCode(HttpStatus.OK.value());

        List<ReservationResponse> actualResponse = response.as(new TypeRef<>() {
        });
        assertThat(actualResponse).hasSize(3);
        assertThat(actualResponse).containsExactlyElementsOf(expectedResponse);

        verify(reservationService, times(1)).getReservations();
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 예약을_삭제한다() {
        // given & when
        long id = 1L;
        Response response = RestAssured
            .given().log().all()
            .pathParam("id", id)
            .when().delete("/reservations/{id}");

        // then
        response
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        verify(reservationService, times(1)).deleteReservation(id);
        verifyNoMoreInteractions(reservationService);
    }

    private Reservation reservation() {
        return new Reservation("이름", LocalDate.now().plusDays(1L), TIME, THEME);
    }

    private ReservationRequest requestDtoFrom(Reservation reservation) {
        return new ReservationRequest(reservation.getName().value(), reservation.getDateValue(),
            reservation.getTime().getId(), reservation.getTheme().getId());
    }
}
