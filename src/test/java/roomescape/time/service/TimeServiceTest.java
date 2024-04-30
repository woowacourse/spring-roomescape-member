package roomescape.time.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import roomescape.exception.ConflictException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.domain.Time;
import roomescape.time.dto.ReservationTimeRequest;
import roomescape.time.dto.ReservationTimeResponse;
import roomescape.time.repository.TimeRepository;

@ExtendWith(MockitoExtension.class)
class TimeServiceTest {
    private final Time time = new Time(1L, LocalTime.of(17, 3));

    @InjectMocks
    private TimeService timeService;
    @Mock
    private TimeRepository timeRepository;
    @Mock
    private ReservationRepository reservationRepository;

    @Test
    @DisplayName("시간을 추가한다.")
    void addReservationTime() {
        Mockito.when(timeRepository.saveReservationTime(any()))
                .thenReturn(time);

        ReservationTimeRequest timeRequest = new ReservationTimeRequest(time.getStartAt());
        ReservationTimeResponse timeResponse = timeService.addReservationTime(timeRequest);

        Assertions.assertThat(timeResponse.id())
                .isEqualTo(1);
    }

    @Test
    @DisplayName("시간을 찾는다.")
    void findReservationTimes() {
        Mockito.when(timeRepository.findAllReservationTimes())
                .thenReturn(List.of(time));

        List<ReservationTimeResponse> timeResponses = timeService.findReservationTimes();

        Assertions.assertThat(timeResponses.size())
                .isEqualTo(1);
    }

    @Test
    @DisplayName("중복된 예약 시간 생성 요청시 예외를 던진다.")
    void validation_ShouldThrowException_WhenStartAtIsDuplicated() {
        Mockito.when(timeRepository.findByStartAt(any()))
                .thenReturn(1);

        assertAll(() -> {
                    Throwable duplicateStartAt = assertThrows(
                            ConflictException.class,
                            () -> timeService.addReservationTime(new ReservationTimeRequest(LocalTime.now())));
                    assertEquals("이미 존재하는 예약 시간입니다.", duplicateStartAt.getMessage());
                }
        );
    }

    @Test
    @DisplayName("시간을 지운다.")
    void removeReservationTime() {
        Mockito.doNothing()
                .when(timeRepository)
                .deleteReservationTimeById(time.getId());

        assertDoesNotThrow(() -> timeService.removeReservationTime(time.getId()));
    }

    @Test
    @DisplayName("예약이 존재하는 예약 시간 삭제 요청시 예외를 던진다.")
    void validateReservationExistence_ShouldThrowException_WhenReservationExistAtTime() {
        Mockito.when(reservationRepository.countReservationByTimeId(1L))
                .thenReturn(1);

        assertAll(() -> {
                    Throwable reservationExistAtTime = assertThrows(
                            ConflictException.class,
                            () -> timeService.removeReservationTime(1L));
                    assertEquals("삭제를 요청한 시간에 예약이 존재합니다.", reservationExistAtTime.getMessage());
                }
        );
    }

}
