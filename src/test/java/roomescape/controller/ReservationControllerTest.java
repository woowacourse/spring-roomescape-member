package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import roomescape.controller.dto.reservation.ReservationRequestDto;
import roomescape.controller.dto.reservation.ReservationResponseDto;
import roomescape.controller.dto.reservation.ReservationsResponseDto;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.vo.MemberName;
import roomescape.domain.vo.ReservationDate;
import roomescape.domain.vo.ThemeImageUrl;
import roomescape.domain.vo.ThemeName;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;
import roomescape.service.ReservationService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationControllerTest {

    private static final ReservationTime TIME = new ReservationTime(1L, LocalTime.now().plusHours(1));
    private static final Theme THEME = new Theme(1L, new ThemeName("name"), "d", ThemeImageUrl.defaultImageUrl());
    private static final Reservation RESERVATION = Reservation.create(
            new MemberName("이름"),
            new ReservationDate(LocalDate.now().plusDays(1L)),
            TIME,
            THEME);

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
        //given
        ReservationRequestDto request = requestDtoFrom(RESERVATION);
        when(reservationService.addReservation(request.toCommand()))
                .thenReturn(RESERVATION.withId(1L));

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

        ReservationResponseDto responseDto = response.as(ReservationResponseDto.class);
        assertThat(responseDto.id()).isEqualTo(1L);
    }

    @Test
    void 관리자가_예약을_삭제한다() {
        // given & when
        Response response = RestAssured
                .given().log().all()
                .pathParam("id", 1)
                .queryParam("role", "admin")
                .when().delete("/reservations/{id}");

        // then
        response
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 사용자가_이름으로_자신의_예약을_삭제한다() {
        // given & when
        Response response = RestAssured
                .given().log().all()
                .pathParam("id", 1)
                .queryParam("name", "브라운")
                .when().delete("/reservations/{id}");

        // then
        response
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 사용자가_타인의_예약을_삭제하려하면_예외가_발생한다() {
        // given
        MemberName otherName = new MemberName("파도");
        doThrow(new BusinessException(ErrorCode.RESERVATION_ACCESS_DENIED))
                .when(reservationService).deleteReservation(1L, otherName);

        // when
        Response response = RestAssured
                .given().log().all()
                .pathParam("id", 1L)
                .queryParam("name", "파도")
                .when().delete("/reservations/{id}");

        // then
        response
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());

        String errorCode = response.jsonPath().getString("code");
        String errorMessage = response.jsonPath().getString("message");

        assertThat(errorMessage).isEqualTo(ErrorCode.RESERVATION_ACCESS_DENIED.getMessage());
    }

    @Test
    void 모든_예약을_조회한다() {
        // given
        List<Reservation> reservations = List.of(RESERVATION.withId(1L), RESERVATION.withId(2L), RESERVATION.withId(3L));
        ReservationsResponseDto expected = new ReservationsResponseDto(reservations.stream()
                .map(ReservationResponseDto::from)
                .toList());

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

        ReservationsResponseDto responseDto = response.as(ReservationsResponseDto.class);
        assertThat(responseDto.reservations()).hasSize(3);
        assertThat(responseDto).isEqualTo(expected);
    }

    @Test
    void 사용자_이름으로_사용자의_모든_예약을_조회한다() {
        // given
        MemberName testName = new MemberName("브라운");
        Reservation reservation = Reservation.create(testName, new ReservationDate(LocalDate.now()), TIME, THEME);
        List<Reservation> reservations = List.of(reservation.withId(1L), reservation.withId(2L), reservation.withId(3L));
        ReservationsResponseDto expected = new ReservationsResponseDto(reservations.stream()
                .map(ReservationResponseDto::from)
                .toList());

        when(reservationService.findReservationsByName(testName))
                .thenReturn(reservations);

        // when
        Response response = RestAssured
                .given().log().all()
                .queryParam("name", "브라운")
                .when().get("/reservations");

        // then
        response
                .then()
                .statusCode(HttpStatus.OK.value());

        ReservationsResponseDto responseDto = response.as(ReservationsResponseDto.class);
        assertThat(responseDto.reservations()).hasSize(3);
        assertThat(responseDto).isEqualTo(expected);
    }

    @Test
    void 사용자가_이름으로_자신의_예약을_수정한다() {
        // given
        MemberName testName = new MemberName("브라운");
        Reservation reservation = new Reservation(1L, testName, new ReservationDate(LocalDate.now()), TIME, THEME);
        ReservationRequestDto request = requestDtoFrom(reservation);

        // when
        Response response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .pathParam("id", reservation.getId())
                .queryParam("name", "브라운")
                .when().put("/reservations/{id}");

        // then
        response
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    private ReservationRequestDto requestDtoFrom(Reservation reservation) {
        return new ReservationRequestDto(reservation.getName().value(), reservation.getDate().value(), reservation.getTime().getId(), reservation.getTheme().getId());
    }
}