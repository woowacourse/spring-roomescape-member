package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.application.dto.TimeDto;
import roomescape.domain.ReservationTime;
import roomescape.domain.repository.TimeRepository;
import roomescape.domain.repository.dto.TimeDataWithBookingInfo;
import roomescape.exception.BusinessException;
import roomescape.exception.NotFoundException;
import roomescape.presentation.dto.request.TimeRequest;

@ExtendWith(MockitoExtension.class)
class TimeServiceTest {

    @Mock
    private TimeRepository timeRepository;

    @InjectMocks
    private TimeService timeService;

    @DisplayName("모든 예약 시간을 조회할 수 있다.")
    @Test
    void getAllTimes() {
        // given
        List<ReservationTime> times = List.of(
                ReservationTime.of(1L, LocalTime.of(10, 0)),
                ReservationTime.of(2L, LocalTime.of(11, 0))
        );
        given(timeRepository.findAll()).willReturn(times);

        // when
        List<TimeDto> result = timeService.getAllTimes();

        // then
        assertThat(result).hasSize(2);
        verify(timeRepository).findAll();
    }

    @DisplayName("새로운 예약 시간을 등록할 수 있다.")
    @Test
    void registerNewTime() {
        // given
        TimeRequest request = new TimeRequest(LocalTime.of(14, 0));
        given(timeRepository.save(any(ReservationTime.class))).willReturn(1L);

        // when
        TimeDto result = timeService.registerNewTime(request);

        // then
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.startAt()).isEqualTo(request.startAt());

        verify(timeRepository).save(any(ReservationTime.class));
    }

    @DisplayName("예약 시간을 삭제할 수 있다.")
    @Test
    void deleteTime_success() {
        // when
        timeService.deleteTime(1L);

        // then
        verify(timeRepository).deleteById(1L);
    }

    @DisplayName("예약이 걸려 있어 시간을 삭제할 수 없으면 비즈니스 예외가 발생한다.")
    @Test
    void deleteTime_fail_dueToForeignKey() {
        // given
        doThrow(DataIntegrityViolationException.class)
                .when(timeRepository).deleteById(1L);

        // then
        assertThatThrownBy(() -> timeService.deleteTime(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("삭제할 수 없습니다");

        verify(timeRepository).deleteById(1L);
    }

    @DisplayName("ID로 예약 시간을 조회할 수 있다.")
    @Test
    void getTimeById_success() {
        // given
        long timeId = 1L;
        LocalTime time = LocalTime.of(10, 0);
        ReservationTime reservationTime = ReservationTime.of(timeId, time);
        given(timeRepository.findById(timeId)).willReturn(Optional.of(reservationTime));

        // when
        ReservationTime result = timeService.getTimeById(timeId);

        // then
        assertThat(result.getStartAt()).isEqualTo(time);
        verify(timeRepository).findById(timeId);
    }

    @DisplayName("ID로 예약 시간을 조회할 수 없으면 NotFoundException이 발생한다.")
    @Test
    void getTimeById_fail() {
        // given
        long notExistId = 99L;
        given(timeRepository.findById(notExistId)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> timeService.getTimeById(notExistId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("찾으려는 시간 id");

        verify(timeRepository).findById(notExistId);
    }

    @DisplayName("예약 여부가 포함된 예약 시간 정보를 조회할 수 있다.")
    @Test
    void getTimesWithBookingInfo() {
        // given
        LocalDate date = LocalDate.of(2025, 5, 2);
        Long themeId = 1L;
        List<TimeDataWithBookingInfo> times = List.of(
                new TimeDataWithBookingInfo(1L, LocalTime.of(10, 0), false),
                new TimeDataWithBookingInfo(2L, LocalTime.of(11, 0), true)
        );
        given(timeRepository.getTimesWithBookingInfo(date, themeId)).willReturn(times);

        // when
        timeService.getTimesWithBookingInfo(date, themeId);

        // then
        verify(timeRepository).getTimesWithBookingInfo(date, themeId);
    }
}
