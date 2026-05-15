package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.util.ArrayList;
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
import roomescape.domain.vo.MemberName;
import roomescape.domain.vo.ReservationLocalDate;
import roomescape.domain.vo.ThemeImageUrl;
import roomescape.domain.vo.ThemeName;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.dto.reservation.ReservationUpdateRequest;
import roomescape.exception.ErrorCode;
import roomescape.exception.ErrorResponse;
import roomescape.exception.RoomEscapeException;
import roomescape.service.ReservationService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationControllerTest {

    private static final ReservationTime TIME = new ReservationTime(1L, "12:00");
    private static final Theme THEME = new Theme(1L, new ThemeName("name"), "description",
        ThemeImageUrl.defaultImageUrl());
    private static final MemberName NAME = new MemberName("name");
    private static final LocalDate TOMORROW = LocalDate.now().plusDays(1);

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
    void 지난_날짜와_시간으로_예약을_추가하는_경우_예외_응답을_반환한다() {
        // given
        RoomEscapeException exception = new RoomEscapeException(ErrorCode.PAST_DATE_RESERVATION);
        ReservationRequest request = new ReservationRequest(
            "name",
            LocalDate.now().minusDays(1),
            1L,
            3L
        );

        doThrow(exception)
            .when(reservationService).addReservation(any());

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

        ErrorResponse actualResponse = response.as(ErrorResponse.class);
        assertThat(actualResponse).isEqualTo(ErrorResponse.of(ErrorCode.PAST_DATE_RESERVATION));

        verify(reservationService, times(1)).addReservation(any());
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 중복_예약이_존재하는_경우_예외_응답을_반환한다() {
        // given
        RoomEscapeException exception = new RoomEscapeException(ErrorCode.DUPLICATED_RESERVATION);
        Reservation reservation = reservation();
        ReservationRequest request = requestDtoFrom(reservation);
        doThrow(exception)
            .when(reservationService).addReservation(any());

        // when
        Response response = RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .when().post("/reservations");

        // then
        response
            .then()
            .statusCode(HttpStatus.CONFLICT.value());

        ErrorResponse actualResponse = response.as(ErrorResponse.class);
        ErrorResponse expectedResponse = ErrorResponse.of(exception.getCode());
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
    void 사용자_이름으로_해당_사용자의_모든_예약을_조회한다() {
        // given
        Reservation reservation = reservation();
        List<Reservation> reservations = new ArrayList<>(List.of(
            reservation.withId(1L), reservation.withId(2L), reservation.withId(3L)));

        MemberName targetName = new MemberName("targetName");
        Reservation targetNameReservation = new Reservation(
            4L, targetName, new ReservationLocalDate(TOMORROW), TIME, THEME);
        reservations.add(targetNameReservation);

        List<Reservation> expectedReservation = reservations.stream()
            .filter(res -> res.getName().equals(targetName))
            .toList();

        List<ReservationResponse> expectedResponse = expectedReservation.stream()
            .map(ReservationResponse::from)
            .toList();

        when(reservationService.getReservations(any()))
            .thenReturn(expectedReservation);

        // when
        Response response = RestAssured
            .given().log().all()
            .queryParam("name", targetName.value())
            .when().get("/reservations");

        // then
        response
            .then()
            .statusCode(HttpStatus.OK.value());

        List<ReservationResponse> actualResponse = response.as(new TypeRef<>() {
        });
        assertThat(actualResponse).hasSize(1);
        assertThat(actualResponse).containsExactlyElementsOf(expectedResponse);

        verify(reservationService, times(1)).getReservations(targetName.value());
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 사용자_이름에_해당되는_예약이_없는_경우_빈_리스트_응답을_반환한다() {
        // given
        when(reservationService.getReservations(any()))
            .thenReturn(List.of());

        String name = "name";

        // when
        Response response = RestAssured
            .given().log().all()
            .queryParam("name", name)
            .when().get("/reservations");

        // then
        response
            .then()
            .statusCode(HttpStatus.OK.value());

        List<ReservationResponse> actualResponse = response.as(new TypeRef<>() {
        });
        assertThat(actualResponse).hasSize(0);

        verify(reservationService, times(1)).getReservations(name);
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 예약을_삭제한다() {
        // given & when
        long id = 1L;
        String name = "korogoo";
        Response response = RestAssured
            .given().log().all()
            .pathParam("id", id)
            .queryParam("name", name)
            .when().delete("/reservations/{id}");

        // then
        response
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        verify(reservationService, times(1)).deleteReservation(id, name);
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 존재하지_않는_예약을_삭제하는_경우_예외_응답을_반환한다() {
        // given
        RoomEscapeException exception = new RoomEscapeException(ErrorCode.RESERVATION_NOT_FOUND);
        doThrow(exception)
            .when(reservationService).deleteReservation(anyLong(), any());

        // when
        long id = 1L;
        String name = "korogoo";
        Response response = RestAssured
            .given().log().all()
            .pathParam("id", id)
            .queryParam("name", name)
            .when().delete("/reservations/{id}");

        // then
        response
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());

        verify(reservationService, times(1)).deleteReservation(id, name);
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 다른_사람의_예약을_삭제하는_경우_예외_응답을_반환한다() {
        // given
        RoomEscapeException exception = new RoomEscapeException(ErrorCode.FORBIDDEN);
        doThrow(exception)
            .when(reservationService).deleteReservation(anyLong(), any());

        // when
        long id = 1L;
        String name = "korogoo";
        Response response = RestAssured
            .given().log().all()
            .pathParam("id", id)
            .queryParam("name", name)
            .when().delete("/reservations/{id}");

        // then
        response
            .then()
            .statusCode(HttpStatus.FORBIDDEN.value());

        verify(reservationService, times(1)).deleteReservation(id, name);
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 오늘_이전의_예약을_삭제하는_경우_예외_응답을_반환한다() {
        // given
        RoomEscapeException exception = new RoomEscapeException(ErrorCode.PAST_RESERVATION_CANCEL);
        doThrow(exception)
            .when(reservationService).deleteReservation(anyLong(), any());

        // when
        long id = 1L;
        String name = "korogoo";
        Response response = RestAssured
            .given().log().all()
            .pathParam("id", id)
            .queryParam("name", name)
            .when().delete("/reservations/{id}");

        // then
        response
            .then()
            .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());

        verify(reservationService, times(1)).deleteReservation(id, name);
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 사용자는_본인_예약의_날짜_및_시간을_변경할_수_있다() {
        //given
        MemberName name = new MemberName("korogoo");
        Reservation reservation = new Reservation(1L, name, new ReservationLocalDate(TOMORROW), TIME, THEME);

        ReservationUpdateRequest request = updateRequestFrom(reservation);

        //when
        Response response = RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .pathParam("id", 1L)
            .queryParam("name", name.value())
            .when().patch("/reservations/{id}");

        //then
        response
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        verify(reservationService, times(1)).updateDateTime(reservation.getId(), name.value(), request);
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 예약의_날짜를_과거_날짜로_변경하는_경우_예외_응답을_반환한다() {
        //given
        RoomEscapeException exception = new RoomEscapeException(ErrorCode.PAST_RESERVATION_UPDATE);

        MemberName name = new MemberName("korogoo");
        Reservation reservation = new Reservation(1L, name, new ReservationLocalDate(TOMORROW), TIME, THEME);

        ReservationUpdateRequest request = updateRequestFrom(reservation);

        doThrow(exception)
            .when(reservationService).updateDateTime(anyLong(), any(), any());
        ErrorResponse expectedErrorResponse = ErrorResponse.of(ErrorCode.PAST_RESERVATION_UPDATE);

        //when
        Response response = RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .pathParam("id", 1L)
            .queryParam("name", name.value())
            .when().patch("/reservations/{id}");

        //then
        response
            .then()
            .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());

        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertThat(errorResponse).isEqualTo(expectedErrorResponse);

        verify(reservationService, times(1)).updateDateTime(reservation.getId(), name.value(), request);
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 존재하지_않는_예약을_변경하는_경우_예외_응답을_반환한다() {
        //given
        RoomEscapeException exception = new RoomEscapeException(ErrorCode.RESERVATION_NOT_FOUND);

        MemberName name = new MemberName("korogoo");
        Reservation reservation = new Reservation(1L, name, new ReservationLocalDate(TOMORROW), TIME, THEME);

        ReservationUpdateRequest request = updateRequestFrom(reservation);

        doThrow(exception)
            .when(reservationService).updateDateTime(anyLong(), any(), any());
        ErrorResponse expectedErrorResponse = ErrorResponse.of(ErrorCode.RESERVATION_NOT_FOUND);

        //when
        Response response = RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .pathParam("id", 1L)
            .queryParam("name", name.value())
            .when().patch("/reservations/{id}");

        //then
        response
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());

        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertThat(errorResponse).isEqualTo(expectedErrorResponse);

        verify(reservationService, times(1)).updateDateTime(reservation.getId(), name.value(), request);
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 예약을_존재하지_않는_시간으로_변경하는_경우_예외_응답을_반환한다() {
        //given
        RoomEscapeException exception = new RoomEscapeException(ErrorCode.TIME_NOT_FOUND);

        MemberName name = new MemberName("korogoo");
        Reservation reservation = new Reservation(1L, name, new ReservationLocalDate(TOMORROW), TIME, THEME);

        ReservationUpdateRequest request = updateRequestFrom(reservation);

        doThrow(exception)
            .when(reservationService).updateDateTime(anyLong(), any(), any());
        ErrorResponse expectedErrorResponse = ErrorResponse.of(ErrorCode.TIME_NOT_FOUND);

        //when
        Response response = RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .pathParam("id", 1L)
            .queryParam("name", name.value())
            .when().patch("/reservations/{id}");

        //then
        response
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());

        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertThat(errorResponse).isEqualTo(expectedErrorResponse);

        verify(reservationService, times(1)).updateDateTime(reservation.getId(), name.value(), request);
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 중복되는_예약으로_변경하는_경우_예외_응답을_반환한다() {
        //given
        RoomEscapeException exception = new RoomEscapeException(ErrorCode.DUPLICATED_RESERVATION);

        MemberName name = new MemberName("korogoo");
        Reservation reservation = new Reservation(1L, name, new ReservationLocalDate(TOMORROW), TIME, THEME);

        ReservationUpdateRequest request = updateRequestFrom(reservation);

        doThrow(exception)
            .when(reservationService).updateDateTime(anyLong(), any(), any());
        ErrorResponse expectedErrorResponse = ErrorResponse.of(ErrorCode.DUPLICATED_RESERVATION);

        //when
        Response response = RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .pathParam("id", 1L)
            .queryParam("name", name.value())
            .when().patch("/reservations/{id}");

        //then
        response
            .then()
            .statusCode(HttpStatus.CONFLICT.value());

        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertThat(errorResponse).isEqualTo(expectedErrorResponse);

        verify(reservationService, times(1)).updateDateTime(reservation.getId(), name.value(), request);
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 다른_사용자의_예약을_변경하는_경우_예외_응답을_반환한다() {
        //given
        RoomEscapeException exception = new RoomEscapeException(ErrorCode.FORBIDDEN);

        MemberName name = new MemberName("korogoo");
        Reservation reservation = new Reservation(1L, name, new ReservationLocalDate(TOMORROW), TIME, THEME);

        ReservationUpdateRequest request = updateRequestFrom(reservation);

        doThrow(exception)
            .when(reservationService).updateDateTime(anyLong(), any(), any());
        ErrorResponse expectedErrorResponse = ErrorResponse.of(ErrorCode.FORBIDDEN);

        //when
        Response response = RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .pathParam("id", 1L)
            .queryParam("name", name.value())
            .when().patch("/reservations/{id}");

        //then
        response
            .then()
            .statusCode(HttpStatus.FORBIDDEN.value());

        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertThat(errorResponse).isEqualTo(expectedErrorResponse);

        verify(reservationService, times(1)).updateDateTime(reservation.getId(), name.value(), request);
        verifyNoMoreInteractions(reservationService);
    }

    @Test
    void 지난_날짜로_예약을_변경하는_경우_예외_응답을_반환한다() {
        //given
        RoomEscapeException exception = new RoomEscapeException(ErrorCode.PAST_DATE_RESERVATION);

        MemberName name = new MemberName("korogoo");
        Reservation reservation = new Reservation(1L, name, new ReservationLocalDate(TOMORROW), TIME, THEME);

        ReservationUpdateRequest request = updateRequestFrom(reservation);

        doThrow(exception)
            .when(reservationService).updateDateTime(anyLong(), any(), any());
        ErrorResponse expectedErrorResponse = ErrorResponse.of(ErrorCode.PAST_DATE_RESERVATION);

        //when
        Response response = RestAssured
            .given().log().all()
            .contentType(ContentType.JSON)
            .body(request)
            .pathParam("id", 1L)
            .queryParam("name", name.value())
            .when().patch("/reservations/{id}");

        //then
        response
            .then()
            .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());

        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertThat(errorResponse).isEqualTo(expectedErrorResponse);

        verify(reservationService, times(1)).updateDateTime(reservation.getId(), name.value(), request);
        verifyNoMoreInteractions(reservationService);
    }

    private Reservation reservation() {
        return new Reservation("이름", LocalDate.now().plusDays(1L), TIME, THEME);
    }

    private ReservationRequest requestDtoFrom(Reservation reservation) {
        return new ReservationRequest(
            reservation.getName().value(),
            reservation.getDateValue(),
            reservation.getTime().getId(),
            reservation.getTheme().getId()
        );
    }

    private ReservationUpdateRequest updateRequestFrom(Reservation reservation) {
        return new ReservationUpdateRequest(reservation.getDateValue(), reservation.getTime().getId());
    }
}
