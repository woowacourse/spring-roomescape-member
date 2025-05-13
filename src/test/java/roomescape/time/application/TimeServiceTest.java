package roomescape.time.application;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.infrastructure.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.infrastructure.ThemeRepository;
import roomescape.time.domain.Time;
import roomescape.time.dto.AvailableTimeRequest;
import roomescape.time.dto.AvailableTimeResponse;
import roomescape.time.dto.TimeRequest;
import roomescape.time.dto.TimeResponse;
import roomescape.time.infrastructure.TimeRepository;

import static org.assertj.core.api.Assertions.*;

class TimeServiceTest {

    private TimeRepository timeRepository;
    private ReservationRepository reservationRepository;
    private ThemeRepository themeRepository;
    private TimeService timeService;

    @BeforeEach
    void setUp() {
        timeRepository = mock(TimeRepository.class);
        reservationRepository = mock(ReservationRepository.class);
        themeRepository = mock(ThemeRepository.class);
        timeService = new TimeService(timeRepository, reservationRepository, themeRepository);
    }

    @Test
    @DisplayName("이미 존재하는 시간이 있으면 예외가 발생한다")
    void test1() {
        // given
        LocalTime startAt = LocalTime.of(10, 0);
        TimeRequest timeRequest = new TimeRequest(startAt);
        when(timeRepository.existsByStartAt(startAt)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> timeService.add(timeRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이미 해당 시간이 존재합니다.");
    }

    @Test
    @DisplayName("시간을 정상적으로 추가한다")
    void test2() {
        // given
        LocalTime startAt = LocalTime.of(10, 0);
        TimeRequest timeRequest = new TimeRequest(startAt);
        Time time = new Time(1L, startAt);

        when(timeRepository.existsByStartAt(startAt)).thenReturn(false);
        when(timeRepository.add(any(Time.class))).thenReturn(time);

        // when
        TimeResponse result = timeService.add(timeRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.startAt()).isEqualTo(startAt);
    }

    @Test
    @DisplayName("전체 시간을 조회하면 저장된 모든 시간이 반환된다")
    void test3() {
        // given
        Time time1 = new Time(1L, LocalTime.of(10, 0));
        Time time2 = new Time(2L, LocalTime.of(14, 0));
        when(timeRepository.findAll()).thenReturn(List.of(time1, time2));

        // when
        List<TimeResponse> result = timeService.findAll();

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("startAt")
                .containsExactly(LocalTime.of(10, 0), LocalTime.of(14, 0));
    }

    @Test
    @DisplayName("특정 날짜와 테마에 대한 시간의 예약 여부를 반환한다")
    void test4() {
        // given
        AvailableTimeRequest availableTimeRequest = new AvailableTimeRequest(LocalDate.now(), 1L);
        Time reservedTime = new Time(1L, LocalTime.of(10, 0));
        Time notReservedTime = new Time(2L, LocalTime.of(12, 0));
        List<AvailableTimeResponse> occupiedTimeIds = List.of(
                new AvailableTimeResponse(reservedTime.id(), reservedTime.startAt(), true),
                new AvailableTimeResponse(notReservedTime.id(), notReservedTime.startAt(), false)
        );

        when(timeRepository.findByDateAndThemeId(any(LocalDate.class), eq(1L))).thenReturn(
                occupiedTimeIds);
        when(themeRepository.findById(any(Long.class))).thenReturn(Optional.of(mock(Theme.class)));

        // when
        List<AvailableTimeResponse> result = timeService.findByDateAndThemeId(availableTimeRequest);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).startAt()).isEqualTo(reservedTime.startAt());
        assertThat(result.get(0).alreadyBooked()).isTrue();
        assertThat(result.get(1).startAt()).isEqualTo(notReservedTime.startAt());
        assertThat(result.get(1).alreadyBooked()).isFalse();
    }

    @Test
    @DisplayName("존재하지 않는 시간을 삭제할 시 예외가 발생한다")
    void test7() {
        // given
        Long timeId = 1L;

        when(timeRepository.findById(timeId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> timeService.deleteById(timeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 해당 id의 시간이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("예약된 시간은 삭제할 수 없다")
    void test6() {
        // given
        Long timeId = 1L;
        Time time = new Time(timeId, LocalTime.of(10, 0));

        when(timeRepository.findById(timeId)).thenReturn(Optional.of(time));
        when(reservationRepository.existsByTimeId(timeId)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> timeService.deleteById(timeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 예약된 시간은 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("예약되지 않고 존재하는 시간을 삭제할 수 있다")
    void test5() {
        // given
        Long timeId = 1L;
        Time time = new Time(timeId, LocalTime.of(10, 0));

        when(timeRepository.findById(timeId)).thenReturn(Optional.of(time));
        when(reservationRepository.existsByTimeId(timeId)).thenReturn(false);

        // when
        timeService.deleteById(timeId);

        // then
        verify(timeRepository).deleteById(timeId);
    }
}
