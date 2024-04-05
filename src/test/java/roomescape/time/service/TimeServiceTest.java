package roomescape.time.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.Time.domain.Time;
import roomescape.Time.dto.ReservationTimeRequest;
import roomescape.Time.dto.ReservationTimeResponse;
import roomescape.Time.repository.TimeRepository;
import roomescape.Time.service.TimeService;

@ExtendWith(MockitoExtension.class)
class TimeServiceTest {
    private final Time time = new Time(1L, LocalTime.of(17, 3));

    @InjectMocks
    private TimeService timeService;
    @Mock
    private TimeRepository timeRepository;

    @Test
    @DisplayName("시간을 추가한다.")
    void addReservationTime() {
        Mockito.when(timeRepository.saveReservationTime(any()))
                .thenReturn(time);

        ReservationTimeRequest timeRequest = new ReservationTimeRequest(time.getStartAt());
        ReservationTimeResponse timeResponse = timeService.addReservationTime(timeRequest);

        Assertions.assertThat(timeResponse.id()).isEqualTo(1);
    }

    @Test
    @DisplayName("시간을 찾는다.")
    void findReservationTimes() {
        Mockito.when(timeRepository.findAllReservationTimes())
                .thenReturn(List.of(time));

        List<ReservationTimeResponse> timeResponses = timeService.findReservationTimes();

        Assertions.assertThat(timeResponses.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("시간을 지운다.")
    void removeReservationTime() {
        Mockito.doNothing()
                .when(timeRepository)
                .deleteReservationTimeById(time.getId());

        assertDoesNotThrow(() -> timeService.removeReservationTime(time.getId()));
    }
}
