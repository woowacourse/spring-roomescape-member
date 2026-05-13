package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.exception.PastReservationNotAllowedException;
import roomescape.exception.ReservationAlreadyExistsException;
import roomescape.exception.ReservationNotFoundException;

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
        when(reservationDao.delete(1L)).thenReturn(0);

        assertThatThrownBy(() -> reservationService.deleteReservation(1L))
                .isInstanceOf(ReservationNotFoundException.class);
    }

    @Test
    void 예약이_존재할_경우_새_예약을_생성할_수_없다() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        when(reservationTimeDao.findReservationTimeById(1L))
                .thenReturn(new ReservationTime(1L, LocalTime.of(10, 0)));
        when(reservationDao.insertWithKeyHolder("정콩이", futureDate, 1L, 1L))
                .thenThrow(new DuplicateKeyException("Duplicate key exception"));

        assertThatThrownBy(
                () -> reservationService.createReservation("정콩이", futureDate, 1L, 1L))
                .isInstanceOf(ReservationAlreadyExistsException.class);
    }

    @Test
    void 지난_날짜로는_새_예약을_생성할_수_없다() {
        when(reservationTimeDao.findReservationTimeById(1L))
                .thenReturn(new ReservationTime(1L, LocalTime.of(10, 0)));
        assertThatThrownBy(
                () -> reservationService.createReservation("정콩이", LocalDate.of(2025, 1, 1), 1L, 1L))
                .isInstanceOf(PastReservationNotAllowedException.class);
    }
}
