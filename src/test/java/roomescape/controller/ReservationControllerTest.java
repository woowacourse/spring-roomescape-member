package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
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
import roomescape.dto.reservation.ReservationRequestDto;
import roomescape.dto.reservation.ReservationResponseDto;
import roomescape.dto.reservation.ReservationsResponseDto;
import roomescape.service.ReservationService;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationControllerTest {

    private static final ReservationTime TIME = new ReservationTime(1L, "12:00");
    private static final Theme THEME = new Theme(1L, new ThemeName("name"), "d", ThemeImageUrl.defaultImageUrl());
    private static final Reservation RESERVATION = Reservation.create(
            "이름",
            LocalDate.now().plusDays(1L),
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
        when(reservationService.addReservation(request))
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
    void 예약을_삭제한다() {
        // given & when
        Response response = RestAssured
                .given().log().all()
                .pathParam("id", 1)
                .when().delete("/reservations/{id}");

        // then
        response
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
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

    private ReservationRequestDto requestDtoFrom(Reservation reservation) {
        return new ReservationRequestDto(reservation.getName().value(), reservation.getDateValue(), reservation.getTime().getId(), reservation.getTheme().getId());
    }
}