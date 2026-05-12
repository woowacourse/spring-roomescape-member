package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.reservation.Reservation;
import roomescape.reservation.dao.ReservationDao;
import roomescape.time.ReservationTime;
import roomescape.time.dao.TimeDao;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationDao reservationDao;

    @Mock
    private TimeDao timeDao;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void 지난_날짜및시간_예약_하는_경우_예외발생() {
        ReservationTime mockTime = new ReservationTime(LocalTime.parse("10:00"));

        when(timeDao.selectById(anyLong())).thenReturn(mockTime);

        List<Reservation> reservations = List.of();
        when(reservationDao.selectByThemeIdAndDate(anyLong(),any(LocalDate.class))).thenReturn(reservations);

        assertThatThrownBy(() -> reservationService.add("브라운", 1L, LocalDate.of(2026, 5, 10), 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 지난 날짜는 예약할 수 없습니다.");

        assertThatThrownBy(() -> reservationService.add("브라운", 1L, LocalDate.now(), 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 지난 시간은 예약할 수 없습니다.");
    }

    @Test
    void 이미_예약이_존재하는_경우_예외발생() {
        ReservationTime mockTime = new ReservationTime(1L, LocalTime.parse("10:00"));
        when(timeDao.selectById(anyLong())).thenReturn(mockTime);

        List<Reservation> reservations = new ArrayList<>();
        Reservation reservation = new Reservation("초록", 1L, LocalDate.of(2026, 5, 20), mockTime);
        reservations.add(reservation);
        when(reservationDao.selectByThemeIdAndDate(anyLong(), any(LocalDate.class))).thenReturn(reservations);

        assertThatThrownBy(() -> reservationService.add("브라운", 1L, LocalDate.of(2026, 5, 20), 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이미 예약이 존재합니다.");
    }
}
