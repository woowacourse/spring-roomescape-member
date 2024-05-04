package roomescape.reservation.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.exception.ConflictException;
import roomescape.reservation.dao.ReservationJdbcDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.theme.domain.Theme;
import roomescape.time.dao.TimeJdbcDao;
import roomescape.time.domain.Time;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    private final Reservation reservation = new Reservation(1L, "polla", LocalDate.MAX,
            new Time(1L, LocalTime.of(12,0)),
            new Theme(1L, "pollaBang", "폴라 방탈출", "thumbnail"));

    @InjectMocks
    private ReservationService reservationService;
    @Mock
    private ReservationJdbcDao reservationJdbcDao;
    @Mock
    private TimeJdbcDao timeJdbcDao;

    @Test
    @DisplayName("예약을 추가한다.")
    void addReservation() {
        Mockito.when(reservationJdbcDao.save(any()))
                .thenReturn(reservation);

        Mockito.when(timeJdbcDao.findById(1L))
                .thenReturn(reservation.getReservationTime());

        ReservationRequest reservationRequest = new ReservationRequest(reservation.getDate(), reservation.getName(),
                reservation.getReservationTime()
                        .getId(), reservation.getTheme()
                .getId());
        ReservationResponse reservationResponse = reservationService.addReservation(reservationRequest);

        Assertions.assertThat(reservationResponse.id())
                .isEqualTo(1);
    }

    @Test
    @DisplayName("예약을 찾는다.")
    void findReservations() {
        Mockito.when(reservationJdbcDao.findAllOrderByDateAndTime())
                .thenReturn(List.of(reservation));

        List<ReservationResponse> reservationResponses = reservationService.findReservations();

        Assertions.assertThat(reservationResponses)
                .hasSize(1);
    }

    @Test
    @DisplayName("예약을 지운다.")
    void removeReservations() {
        Mockito.doNothing()
                .when(reservationJdbcDao)
                .deleteById(reservation.getId());

        assertDoesNotThrow(() -> reservationService.removeReservations(reservation.getId()));
    }

    @Test
    @DisplayName("특정 테마의 예약이 존재하는 시간에 예약을 요청할 때 예외를 던진다.")
    void addReservation_ShouldThrowException_WhenDuplicatedReservationRequestOccurs() {
        Mockito.when(reservationJdbcDao.findAllByThemeIdAndDate(1L, LocalDate.MAX))
                .thenReturn(List.of(reservation));

        Mockito.when(timeJdbcDao.findById(1L))
                .thenReturn(reservation.getReservationTime());

        ReservationRequest reservationRequest = new ReservationRequest(
                reservation.getDate(),
                reservation.getName(),
                reservation.getReservationTimeId(),
                reservation.getThemeId()
        );

        assertThrows(ConflictException.class, () ->
                reservationService.addReservation(reservationRequest)
        );
    }

}
