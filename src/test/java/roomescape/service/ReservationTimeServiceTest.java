package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dto.response.AvailableTimeResponse;
import roomescape.model.ReservationTime;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ReservedChecker;
import roomescape.repository.ReservedTimeChecker;

class ReservationTimeServiceTest {

    private ReservationTimeRepository reservationTimeRepository;
    private ReservedTimeChecker reservedTimeChecker;
    private ReservedChecker reservedChecker;
    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setUp() {
        reservationTimeRepository = mock(ReservationTimeRepository.class);
        reservedTimeChecker = mock(ReservedTimeChecker.class);
        reservedChecker = mock(ReservedChecker.class);
        reservationTimeService = new ReservationTimeService(
                reservationTimeRepository,
                reservedTimeChecker,
                reservedChecker
        );
    }

    @Test
    @DisplayName("예약 시간을 추가할 수 있다.")
    void addTimeTest() {
        LocalTime startAt = LocalTime.of(14, 0);
        ReservationTime toAdd = new ReservationTime(null, startAt);
        ReservationTime saved = new ReservationTime(1L, startAt);

        when(reservationTimeRepository.addTime(any())).thenReturn(saved);

        ReservationTime result = reservationTimeService.addTime(startAt);

        assertThat(result).isEqualTo(saved);
    }

    @Test
    @DisplayName("모든 예약 시간을 조회할 수 있다.")
    void getAllTimeTest() {
        ReservationTime t1 = new ReservationTime(1L, LocalTime.of(10, 0));
        ReservationTime t2 = new ReservationTime(2L, LocalTime.of(14, 0));
        when(reservationTimeRepository.getAllTime()).thenReturn(List.of(t1, t2));

        List<ReservationTime> result = reservationTimeService.getAllTime();

        assertThat(result).containsExactly(t1, t2);
    }

    @Test
    @DisplayName("예약된 시간 삭제 시 예외가 발생한다.")
    void deleteReservedTimeTest() {
        when(reservedTimeChecker.isReservedTime(1L)).thenReturn(true);

        assertThatThrownBy(() -> reservationTimeService.deleteTime(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Reservation time is already reserved.");
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간 삭제 시 예외가 발생한다.")
    void deleteNonExistentTimeTest() {
        when(reservedTimeChecker.isReservedTime(1L)).thenReturn(false);
        when(reservationTimeRepository.deleteTime(1L)).thenReturn(0);

        assertThatThrownBy(() -> reservationTimeService.deleteTime(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("삭제할 시간이 존재하지 않습니다");
    }

    @Test
    @DisplayName("예약 시간 ID로 조회할 수 있다.")
    void getTimeByIdTest() {
        ReservationTime expected = new ReservationTime(1L, LocalTime.of(12, 0));
        when(reservationTimeRepository.findById(1L)).thenReturn(Optional.of(expected));

        ReservationTime result = reservationTimeService.getReservationTimeById(1L);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회 시 예외가 발생한다.")
    void getTimeByInvalidIdTest() {
        when(reservationTimeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationTimeService.getReservationTimeById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 시간입니다");
    }

    @Test
    @DisplayName("예약 가능 시간 목록을 조회할 수 있다.")
    void getAvailableTimesTest() {
        Long themeId = 1L;
        LocalDate date = LocalDate.now();
        ReservationTime t1 = new ReservationTime(1L, LocalTime.of(10, 0));
        ReservationTime t2 = new ReservationTime(2L, LocalTime.of(14, 0));

        when(reservationTimeRepository.getAllTime()).thenReturn(List.of(t1, t2));
        when(reservedChecker.contains(date, 1L, themeId)).thenReturn(true);  // 예약됨
        when(reservedChecker.contains(date, 2L, themeId)).thenReturn(false); // 예약 안됨

        List<AvailableTimeResponse> result = reservationTimeService.getAvailableTimes(themeId, date);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).alreadyBooked()).isTrue();
        assertThat(result.get(1).alreadyBooked()).isFalse();
    }
}
