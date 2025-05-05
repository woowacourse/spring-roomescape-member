package roomescape.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.application.dto.ReservationTimeAvailableResponse;
import roomescape.application.dto.ReservationTimeRequest;
import roomescape.application.dto.ReservationTimeResponse;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @Mock
    private ReservationDao reservationDao;

    @Mock
    private ReservationTimeDao reservationTimeDao;

    @Test
    @DisplayName("모든 예약 시간을 조회한다")
    void findAllReservationTimes() {
        // given
        List<ReservationTime> reservationTimes = sampleReservationTimes();

        doReturn(reservationTimes).when(reservationTimeDao)
                .findAll();

        // when
        List<ReservationTimeResponse> responses = reservationTimeService.findAllReservationTimes();

        // then
        assertThat(responses)
                .hasSize(reservationTimes.size())
                .extracting("id", "startAt")
                .containsExactly(
                        tuple(1L, "10:00"),
                        tuple(2L, "12:00"),
                        tuple(3L, "14:00")
                );

        // verify
        verify(reservationTimeDao, times(1)).findAll();
    }

    @Test
    @DisplayName("예약 가능한 시간을 조회한다")
    void findAvailableTimes() {
        // given
        LocalDate now = LocalDate.now();
        List<ReservationTime> reservationTimes = sampleReservationTimes();

        doReturn(reservationTimes).when(reservationTimeDao)
                .findAll();

        doReturn(false).when(reservationDao)
                .existByDateAndTimeIdAndThemeId(eq(now), any(Long.class), eq(1L));

        // when
        List<ReservationTimeAvailableResponse> responses = reservationTimeService.findAvailableTimes(
                now, 1L);

        // then
        assertThat(responses)
                .hasSize(reservationTimes.size())
                .extracting("id", "startAt", "alreadyBooked")
                .containsExactly(
                        tuple(1L, "10:00", false),
                        tuple(2L, "12:00", false),
                        tuple(3L, "14:00", false)
                );

        // verify
        verify(reservationTimeDao, times(1)).findAll();
        verify(reservationDao, times(3)).existByDateAndTimeIdAndThemeId(eq(now), any(Long.class),
                eq(1L));
    }

    @Test
    @DisplayName("예약 시간을 추가한다")
    void createReservationTime() {
        // given
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(10, 0));
        ReservationTime reservationTime = request.toTime();

        doReturn(1L).when(reservationTimeDao).save(any(ReservationTime.class));

        // when
        ReservationTimeResponse response = reservationTimeService.createReservationTime(request);

        // then
        assertThat(response)
                .extracting("id", "startAt")
                .contains(1L, "10:00");

        // verify
        verify(reservationTimeDao, times(1)).save(any(ReservationTime.class));
    }

    @Test
    @DisplayName("예약 시간을 삭제한다")
    void deleteReservationTime() {
        // given
        long timeId = 1L;
        doReturn(false).when(reservationDao).existByTimeId(timeId);

        // when
        reservationTimeService.deleteReservationTime(timeId);

        // then
        verify(reservationTimeDao, times(1)).deleteById(timeId);
        verify(reservationDao, times(1)).existByTimeId(timeId);
    }

    private List<ReservationTime> sampleReservationTimes() {
        return List.of(
                new ReservationTime(1L, LocalTime.of(10, 0)),
                new ReservationTime(2L, LocalTime.of(12, 0)),
                new ReservationTime(3L, LocalTime.of(14, 0))
        );
    }
}
