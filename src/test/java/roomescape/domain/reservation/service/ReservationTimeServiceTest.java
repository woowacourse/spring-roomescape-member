package roomescape.domain.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.repository.ReservationTimeRepository;
import roomescape.domain.reservation.request.ReservationTimeCreateRequest;
import roomescape.domain.reservation.response.ReservationTimeResponse;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    @Mock
    ReservationTimeRepository reservationTimeRepository;

    @InjectMocks
    ReservationTimeService reservationTimeService;


    @Test
    @DisplayName("예약 시간을 성공적으로 생성한다.")
    void saveReservationTime() {
        // given
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(LocalTime.of(10, 0));

        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        when(reservationTimeRepository.save(any(ReservationTime.class)))
                .thenReturn(time);

        // when
        ReservationTimeResponse response = reservationTimeService.saveReservationTime(request);

        // then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.startAt()).isEqualTo(LocalTime.of(10, 0));

        verify(reservationTimeRepository).save(any(ReservationTime.class));
    }

    @Test
    @DisplayName("모든 예약 시간을 조회한다.")
    void findAllReservationTimes() {
        // given
        ReservationTime time1 = new ReservationTime(1L, LocalTime.of(10, 0));
        ReservationTime time2 = new ReservationTime(1L, LocalTime.of(11, 0));

        List<ReservationTime> reservationTimes = List.of(time1, time2);

        when(reservationTimeRepository.findAll())
                .thenReturn(reservationTimes);

        // when
        List<ReservationTimeResponse> responses = reservationTimeService.findAllReservationTimes();

        // then
        assertThat(responses).hasSize(2)
                .extracting("startAt")
                .containsExactly(LocalTime.of(10, 0), LocalTime.of(11, 0));

        verify(reservationTimeRepository).findAll();
    }

    @Test
    @DisplayName("예약 시간을 삭제한다.")
    void deleteReservationTimeBy() {
        // given
        Long timeId = 1L;

        // when
        reservationTimeService.deleteReservationTimeBy(timeId);

        // then
        verify(reservationTimeRepository).deleteById(timeId);
    }
}
