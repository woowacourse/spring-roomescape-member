package roomescape.reservationTime.service;

import static org.assertj.core.api.Assertions.assertThat;
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
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationTime.dao.ReservationTimeDao;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.dto.admin.ReservationTimeRequest;
import roomescape.reservationTime.dto.user.AvailableReservationTimeRequest;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.domain.Theme;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    private Dao<ReservationTime> reservationTimeDao;
    private Dao<Theme> themeDao;
    private Dao<Reservation> reservationDao;

    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setUp() {
        reservationTimeDao = mock(ReservationTimeDao.class);
        themeDao = mock(ThemeDao.class);
        reservationDao = mock(ReservationDao.class);

        reservationTimeService = new ReservationTimeService(reservationTimeDao, reservationDao, themeDao);
    }

    @DisplayName("시간 내역을 조회하는 기능을 구현한다")
    @Test
    void findAll() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        when(reservationTimeDao.findAll()).thenReturn(List.of(reservationTime));

        assertThat(reservationTimeService.findAll()).hasSize(1);

        verify(reservationTimeDao, times(1)).findAll();
    }

    @DisplayName("시간 내역을 날짜와 테마로 조회 시, 테마 아이다가 존재하지 않는 경우 예외를 발생시킨다")
    @Test
    void exception_find_invalid_themeId() {
        when(themeDao.findById(1L)).thenReturn(Optional.empty());

        AvailableReservationTimeRequest availableReservationTimeRequest = new AvailableReservationTimeRequest(
                LocalDate.now(), 1L
        );

        assertThatThrownBy(() -> reservationTimeService.findByDateAndTheme(availableReservationTimeRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 존재하지 않는 테마 아이디입니다");
    }

    @DisplayName("시간 내역을 추가 시 중복되는 시간인 경우 예외를 발생시킨다")
    @Test
    void exception_add_duplicate_timeId() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(11, 0));
        when(reservationTimeDao.findAll()).thenReturn(List.of(reservationTime));

        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(11, 0));

        assertThatThrownBy(() -> reservationTimeService.add(reservationTimeRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이미 해당 시간이 존재합니다");
    }

    @DisplayName("시간 내역을 삭제 시 존재하지 않는 시간 아이디인 경우 예외를 발생시킨다")
    @Test
    void exception_delete_invalid_timeId() {
        when(reservationTimeDao.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationTimeService.deleteById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 존재하지 않는 시간 아이디입니다");
    }

    @DisplayName("시간 내역을 삭제 시 이미 예약된 시간 아이디인 경우 예외를 발생시킨다")
    @Test
    void exception_delete_occupied_timeId() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        when(reservationTimeDao.findById(1L)).thenReturn(Optional.of(reservationTime));

        Reservation reservation = new Reservation(1L, "teddy", LocalDate.now(), reservationTime,
                new Theme(1L, "name1", "description1", "thumbnail1")
        );
        when(reservationDao.findAll()).thenReturn(List.of(reservation));

        assertThatThrownBy(() -> reservationTimeService.deleteById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이미 예약된 시간은 삭제할 수 없습니다");
    }
}
