package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.dao.ReservationTimeDao;
import roomescape.exception.ReservationTimeInUseException;
import roomescape.exception.ReservationTimeNotFoundException;
import roomescape.exception.UnauthorizedException;

public class ReservationTimeServiceTest {

    private ReservationTimeDao reservationTimeDao;
    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setUp() {
        reservationTimeDao = mock(ReservationTimeDao.class);
        reservationTimeService = new ReservationTimeService(reservationTimeDao);
    }

    @Test
    void 관리자가_아니면_시간을_생성할_수_없다() {
        assertThatThrownBy(() -> reservationTimeService.createReservationTime(
                LocalTime.parse("10:00"), "정콩이")).
                isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void 관리자가_아니면_시간을_삭제할_수_없다() {
        when(reservationTimeDao.delete(1L)).thenReturn(1);
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(1L, "정콩이"))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void 예약이_있는_시간을_삭제할_수_없다() {
        when(reservationTimeDao.delete(1L))
                .thenThrow(new DataIntegrityViolationException("foreign key violation"));

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(1L, "ADMIN"))
                .isInstanceOf(ReservationTimeInUseException.class);
    }

    @Test
    void 존재하지않는_시간을_삭제할_수_없다() {
        when(reservationTimeDao.delete(1L)).thenReturn(0);

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(1L, "ADMIN"))
                .isInstanceOf(ReservationTimeNotFoundException.class);
    }
}
