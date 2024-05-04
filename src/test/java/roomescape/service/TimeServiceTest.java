package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.dao.TimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.AvailableTimeResponse;
import roomescape.dto.TimeCreateRequest;
import roomescape.dto.TimeResponse;

@ExtendWith(MockitoExtension.class)
class TimeServiceTest {
    @Mock
    private TimeDao timeDao;
    @InjectMocks
    private TimeService timeService;

    @DisplayName("예약 시간을 모두 조회할 수 있다.")
    @Test
    void readTimesTest() {
        given(timeDao.readTimes()).willReturn(List.of(
                new ReservationTime(1L, LocalTime.of(19, 0)),
                new ReservationTime(2L, LocalTime.of(10, 0))));

        assertThat(timeService.readTimes()).isEqualTo(List.of(
                new TimeResponse(1L, LocalTime.of(19, 0)),
                new TimeResponse(2L, LocalTime.of(10, 0))));
    }

    @DisplayName("예약 시간의 예약 가능 여부를 조회할 수 있다.")
    @Test
    void readAvailableTimesTest() {
        LocalDate date = LocalDate.of(2023, 8, 20);
        Long themeId = 1L;
        given(timeDao.readTimes()).willReturn(List.of(
                new ReservationTime(1L, LocalTime.of(19, 0)),
                new ReservationTime(2L, LocalTime.of(10, 0))));
        given(timeDao.readTimesExistsReservationDateAndThemeId(date, themeId)).willReturn(List.of(
                new ReservationTime(1L, LocalTime.of(19, 0))));

        assertThat(timeService.readAvailableTimes(date, themeId)).isEqualTo(List.of(
                new AvailableTimeResponse(new TimeResponse(1L, LocalTime.of(19, 0)), true),
                new AvailableTimeResponse(new TimeResponse(2L, LocalTime.of(10, 0)), false)));
    }

    @DisplayName("예약 시간을 생성할 수 있다.")
    @Test
    void createTimeTest() {
        TimeCreateRequest request = new TimeCreateRequest(LocalTime.of(19, 0));
        given(timeDao.isExistTimeByStartAt(LocalTime.of(19, 0))).willReturn(false);
        given(timeDao.createTime(request.createReservationTime()))
                .willReturn(new ReservationTime(1L, LocalTime.of(19, 0)));

        assertThat(timeService.createTime(request)).isEqualTo(new TimeResponse(1L, LocalTime.of(19, 0)));
    }

    @DisplayName("이미 존재하는 예약 시간이면, 예외를 던진다.")
    @Test
    void createTimeTest_whenTimeAlreadyExist() {
        TimeCreateRequest request = new TimeCreateRequest(LocalTime.of(19, 0));
        given(timeDao.isExistTimeByStartAt(LocalTime.of(19, 0))).willReturn(true);

        assertThatThrownBy(() -> timeService.createTime(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 시간은 이미 존재합니다.");
    }
}
