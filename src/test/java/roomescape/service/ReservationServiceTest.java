package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.exception.PastReservationCancelNotAllowedException;
import roomescape.exception.PastReservationNotAllowedException;
import roomescape.exception.ReservationAlreadyExistsException;
import roomescape.exception.ReservationNotFoundException;
import roomescape.exception.ReservationOwnerMismatchException;
import roomescape.exception.ReservationTimeNotFoundException;

public class ReservationServiceTest {

    private ReservationDao reservationDao;
    private ReservationTimeDao reservationTimeDao;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationDao = mock(ReservationDao.class);
        reservationTimeDao = mock(ReservationTimeDao.class);
        reservationService = new ReservationService(reservationDao, reservationTimeDao);
    }

    @Test
    void 존재하지않는_예약은_삭제할_수_없다() {
        when(reservationDao.findReservationById(3L))
                .thenThrow(new EmptyResultDataAccessException(1));

        assertThatThrownBy(() -> reservationService.deleteReservation(3L))
                .isInstanceOf(ReservationNotFoundException.class);
    }

    @Test
    void 예약이_존재할_경우_새_예약을_생성할_수_없다() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        when(reservationTimeDao.findReservationTimeById(1L))
                .thenReturn(new ReservationTime(
                        1L,
                        LocalTime.of(10, 0)
                ));
        when(reservationDao.insertWithKeyHolder("정콩이", futureDate, 1L, 1L))
                .thenThrow(new DuplicateKeyException("Duplicate key exception"));

        assertThatThrownBy(() -> reservationService.createReservation("정콩이", futureDate, 1L, 1L))
                .isInstanceOf(ReservationAlreadyExistsException.class);
    }

    @Test
    void 지난_날짜로는_새_예약을_생성할_수_없다() {
        when(reservationTimeDao.findReservationTimeById(1L))
                .thenReturn(new ReservationTime(
                        1L,
                        LocalTime.of(10, 0)
                ));
        assertThatThrownBy(() -> reservationService.createReservation("정콩이", LocalDate.of(2025, 1, 1), 1L, 1L))
                .isInstanceOf(PastReservationNotAllowedException.class);
    }

    @Test
    void 이미_지난_예약은_취소할_수_없다() {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        when(reservationDao.findReservationById(1L))
                .thenReturn(new Reservation(
                        1L,
                        "정콩이",
                        pastDate,
                        new ReservationTime(1L, LocalTime.of(10, 0)),
                        1L
                ));
        assertThatThrownBy(() -> reservationService.deleteReservation(1L))
                .isInstanceOf(PastReservationCancelNotAllowedException.class);
    }

    @Test
    void 예약자_이름이_일치하지_않으면_예약을_변경할_수_없다() {
        Long reservationId = 1L;
        Long timeId = 2L;
        LocalDate futureDate = LocalDate.now().plusDays(1);

        when(reservationTimeDao.findReservationTimeById(timeId))
                .thenReturn(new ReservationTime(
                        timeId,
                        LocalTime.of(11, 0)
                ));
        when(reservationDao.findReservationById(reservationId))
                .thenReturn(new Reservation(
                        reservationId,
                        "브라운",
                        futureDate,
                        new ReservationTime(1L, LocalTime.of(10, 0)),
                        1L
                ));

        assertThatThrownBy(() -> reservationService.updateReservation(
                reservationId,
                futureDate,
                "정콩이",
                timeId
        )).isInstanceOf(ReservationOwnerMismatchException.class);
    }

    @Test
    void 존재하지않는_예약시간으로는_예약을_변경할_수_없다() {
        Long reservationId = 1L;
        Long timeId = 999L;
        LocalDate futureDate = LocalDate.now().plusDays(1);

        when(reservationTimeDao.findReservationTimeById(timeId))
                .thenThrow(new EmptyResultDataAccessException(1));

        assertThatThrownBy(() -> reservationService.updateReservation(
                reservationId,
                futureDate,
                "브라운",
                timeId
        )).isInstanceOf(ReservationTimeNotFoundException.class);
    }

    @Test
    void 이미_예약된_시간으로는_예약을_변경할_수_없다() {
        Long reservationId = 1L;
        Long timeId = 2L;
        LocalDate futureDate = LocalDate.now().plusDays(1);

        when(reservationTimeDao.findReservationTimeById(timeId))
                .thenReturn(new ReservationTime(
                        timeId,
                        LocalTime.of(11, 0)
                ));
        when(reservationDao.findReservationById(reservationId))
                .thenReturn(new Reservation(
                        reservationId,
                        "브라운",
                        futureDate,
                        new ReservationTime(1L, LocalTime.of(10, 0)),
                        1L
                ));
        when(reservationDao.updateById(reservationId, futureDate, timeId))
                .thenThrow(new DuplicateKeyException("Duplicate key exception"));

        assertThatThrownBy(() -> reservationService.updateReservation(
                reservationId,
                futureDate,
                "브라운",
                timeId
        )).isInstanceOf(ReservationAlreadyExistsException.class);
    }

    @Test
    void 존재하지않는_예약은_변경할_수_없다() {
        Long reservationId = 1L;
        Long timeId = 2L;
        LocalDate futureDate = LocalDate.now().plusDays(1);

        when(reservationTimeDao.findReservationTimeById(timeId))
                .thenReturn(new ReservationTime(
                        timeId,
                        LocalTime.of(11, 0)
                ));
        when(reservationDao.findReservationById(reservationId))
                .thenThrow(new EmptyResultDataAccessException(1));

        assertThatThrownBy(() -> reservationService.updateReservation(
                reservationId,
                futureDate,
                "브라운",
                timeId
        )).isInstanceOf(ReservationNotFoundException.class);
    }

    @Test
    void 지난_날짜로는_예약을_변경할_수_없다() {
        Long timeId = 1L;
        Long reservationId = 2L;
        LocalDate pastDate = LocalDate.now().minusDays(1);

        when(reservationTimeDao.findReservationTimeById(timeId))
                .thenReturn(new ReservationTime(
                        timeId,
                        LocalTime.of(11, 0)
                ));
        when(reservationDao.findReservationById(reservationId))
                .thenReturn(new Reservation(
                        reservationId,
                        "브라운",
                        LocalDate.now().plusDays(1),
                        new ReservationTime(1L, LocalTime.of(10, 0)),
                        1L
                ));

        assertThatThrownBy(() -> reservationService.updateReservation(
                reservationId,
                pastDate,
                "브라운",
                timeId
        )).isInstanceOf(PastReservationNotAllowedException.class);
    }
}
