package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import roomescape.dao.ReservationDao;
import roomescape.dao.TimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.TimeCreateRequest;
import roomescape.dto.response.AvailableTimeResponse;
import roomescape.dto.response.TimeResponse;

@ExtendWith(MockitoExtension.class)
class TimeServiceTest {
    private final ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
    private final ReservationTime time2 = new ReservationTime(1L, LocalTime.of(11, 0));
    @Mock
    TimeDao timeDao;
    @Mock
    ReservationDao reservationDao;
    @InjectMocks
    TimeService timeService;

    @DisplayName("시간 목록을 읽을 수 있다.")
    @Test
    void readTimes() {
        lenient().when(timeDao.readTimes()).thenReturn(List.of(time));

        List<TimeResponse> expected = List.of(TimeResponse.from(time));
        assertThat(timeService.readTimes()).isEqualTo(expected);
    }

    @DisplayName("사용 가능한 시간 목록을 읽을 수 있다.")
    @Test
    void readAvailableTimes() {
        List<ReservationTime> allTimes = List.of(time, time2);
        List<ReservationTime> times = List.of(time);

        lenient().when(timeDao.readTimes()).thenReturn(allTimes);
        lenient().when(timeDao.readTimesExistsReservation(any(String.class), any(Long.class))).thenReturn(times);

        List<AvailableTimeResponse> expected = List.of(
                AvailableTimeResponse.of(time, true),
                AvailableTimeResponse.of(time2, false)
        );
        assertThat(timeService.readAvailableTimes("2024-05-02", 1L)).isEqualTo(expected);
    }

    @DisplayName("시간을 추가할 수 있다.")
    @Test
    void createTime() {
        TimeCreateRequest request = new TimeCreateRequest(LocalTime.of(10, 0));

        lenient().when(timeDao.createTime(any(ReservationTime.class))).thenReturn(time);
        assertThatCode(() -> timeService.createTime(request))
                .doesNotThrowAnyException();
    }

    @DisplayName("시간을 삭제할 수 있다.")
    @Test
    void deleteTime() {
        lenient().when(reservationDao.existsReservationByTimeId(any(Long.class)))
                .thenReturn(false);

        assertThatCode(() -> timeService.deleteTime(1L))
                .doesNotThrowAnyException();
    }

    @DisplayName("예약이 있는 시간인 경우, 삭제하려고 하면 예외를 던진다.")
    @Test
    void deleteTime_whenExistsReservation() {
        lenient().when(reservationDao.existsReservationByTimeId(any(Long.class)))
                .thenReturn(true);

        assertThatThrownBy(() -> timeService.deleteTime(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 시간을 사용하는 예약이 존재합니다.");
    }
}
