package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.dao.ReservationTimeDao;
import roomescape.exception.ReservationTimeInUseException;
import roomescape.exception.ReservationTimeNotFoundException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReservationTimeServiceTest {

    private ReservationTimeDao reservationTimeDao;
    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setUp() {
        reservationTimeDao = mock(ReservationTimeDao.class);
        reservationTimeService = new ReservationTimeService(reservationTimeDao);
    }

    @Test
    void 예약이_있는_시간을_삭제할_수_없다() {
        when(reservationTimeDao.delete(1L))
                .thenThrow(new ReservationTimeInUseException());

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(1L))
                .isInstanceOf(ReservationTimeInUseException.class);
    }

    @Test
    void 존재하지않는_시간을_삭제할_수_없다() {
        when(reservationTimeDao.delete(1L)).thenReturn(0);

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(1L))
                .isInstanceOf(ReservationTimeNotFoundException.class);
    }

    @Test
    void 시간_삭제() {
        when(reservationTimeDao.delete(1L)).thenReturn(1);

        reservationTimeService.deleteReservationTime(1L);

        verify(reservationTimeDao).delete(1L);
    }
}
