package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import roomescape.dao.ReservationDao;
import roomescape.exception.ReservationAlreadyExistsException;
import roomescape.exception.ReservationNotFoundException;

public class ReservationServiceTest {

    private ReservationDao reservationDao;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationDao = mock(ReservationDao.class);
        reservationService = new ReservationService(reservationDao);
    }

    @Test
    void 존재하지않는_예약은_삭제할_수_없다() {
        when(reservationDao.delete(1L)).thenReturn(0);

        assertThatThrownBy(() -> reservationService.deleteReservation(1L))
                .isInstanceOf(ReservationNotFoundException.class);
    }

    @Test
    void 예약이_존재할_경우_새_예약을_생성할_수_없다() {
        when(reservationDao.insertWithKeyHolder("정콩이", LocalDate.of(2025, 3, 2), 1L, 1L))
                .thenThrow(new DuplicateKeyException("Duplicate key exception"));

        assertThatThrownBy(
                () -> reservationService.createReservation("정콩이", LocalDate.of(2025, 3, 2), 1L, 1L))
                .isInstanceOf(ReservationAlreadyExistsException.class);
    }
}
