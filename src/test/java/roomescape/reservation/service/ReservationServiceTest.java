package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.common.Dao;
import roomescape.common.exception.DuplicateException;
import roomescape.common.exception.InvalidIdException;
import roomescape.common.exception.InvalidTimeException;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservationTime.dao.ReservationTimeDao;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.domain.Theme;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    private Dao<ReservationTime> reservationTimeDao;
    private Dao<Theme> themeDao;
    private Dao<Reservation> reservationDao;

    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationTimeDao = mock(ReservationTimeDao.class);
        reservationDao = mock(ReservationDao.class);
        themeDao = mock(ThemeDao.class);

        reservationService = new ReservationService(reservationDao, reservationTimeDao, themeDao);
    }

    @DisplayName("예약 추가 시 현재보다 과거 시간인 경우 예외를 발생시킨다")
    @Test
    void exception_time_before() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.now().minusHours(1));
        when(reservationTimeDao.findById(1L)).thenReturn(Optional.of(reservationTime));

        ReservationRequest reservationRequest = new ReservationRequest("lee", LocalDate.now(), 1L, 1L);
        assertThatThrownBy(() -> reservationService.add(reservationRequest))
                .isInstanceOf(InvalidTimeException.class);

        verify(reservationTimeDao, times(1)).findById(1L);
    }

    @DisplayName("예약 추가 시 동일 일자와 시간에 예약이 존재하는 경우 예외를 발생시킨다")
    @Test
    void exception_not_available() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        when(reservationTimeDao.findById(1L)).thenReturn(Optional.of(reservationTime));

        Theme theme = new Theme(1L, "theme1", "description1", "picture1");
        Reservation reservation = new Reservation(1L, "kim", LocalDate.of(2025, 5, 5), reservationTime, theme);
        when(reservationDao.findAll()).thenReturn(List.of(reservation));

        ReservationRequest reservationRequest = new ReservationRequest("lee", LocalDate.of(2025, 5, 5), 1L, 1L);
        assertThatThrownBy(() -> reservationService.add(reservationRequest))
                .isInstanceOf(DuplicateException.class);

        verify(reservationTimeDao, times(1)).findById(1L);
        verify(reservationDao, times(1)).findAll();
    }

    @DisplayName("예약 추가 시 존재하지 않는 시간 아이디를 조회하려고 할 때 예외를 발생시킨다")
    @Test
    void exception_invalid_time_id() {
        when(reservationTimeDao.findById(2L)).thenReturn(Optional.empty());

        ReservationRequest reservationRequest = new ReservationRequest("lee", LocalDate.now(), 2L, 1L);
        assertThatThrownBy(() -> reservationService.add(reservationRequest))
                .isInstanceOf(InvalidIdException.class);

        verify(reservationTimeDao, times(1)).findById(2L);
    }

    @DisplayName("예약 추가 시 존재하지 않는 테마 아이디를 조회하려고 할 때 예외를 발생시킨다")
    @Test
    void exception_invalid_theme_id() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        when(reservationTimeDao.findById(1L)).thenReturn(Optional.of(reservationTime));
        when(themeDao.findById(2L)).thenReturn(Optional.empty());

        ReservationRequest reservationRequest = new ReservationRequest("lee", LocalDate.of(2025, 5, 5), 1L, 2L);
        assertThatThrownBy(() -> reservationService.add(reservationRequest))
                .isInstanceOf(InvalidIdException.class);

        verify(reservationTimeDao, times(1)).findById(1L);
        verify(themeDao, times(1)).findById(2L);
    }

    @DisplayName("예약 삭제 시 예약 아이디가 존재하지 않는 경우 예외를 발생시킨다")
    @Test
    void exception_invalid_id() {
        assertThatThrownBy(() -> reservationService.deleteById(1L))
                .isInstanceOf(InvalidIdException.class);

        verify(reservationDao, times(1)).findById(1L);
    }
}
