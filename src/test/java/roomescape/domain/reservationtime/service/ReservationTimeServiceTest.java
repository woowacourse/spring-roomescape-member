package roomescape.domain.reservationtime.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.domain.reservationtime.entity.ReservationTime;
import roomescape.domain.reservationtime.exception.ReservationTimeDeleteConflictException;
import roomescape.domain.reservationtime.repository.ReservationTimeRepository;
import roomescape.domain.reservationtime.request.ReservationTimeCreateRequest;
import roomescape.domain.reservationtime.response.ReservationTimeResponse;
import roomescape.domain.reservationtime.response.ReservationTimesResponse;

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

        ReservationTime time = ReservationTime.of(1L, LocalTime.of(10, 0));
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
        ReservationTime time1 = ReservationTime.of(1L, LocalTime.of(10, 0));
        ReservationTime time2 = ReservationTime.of(1L, LocalTime.of(11, 0));

        List<ReservationTime> reservationTimes = List.of(time1, time2);

        when(reservationTimeRepository.findAll())
                .thenReturn(reservationTimes);

        // when
        ReservationTimesResponse foundTimes = reservationTimeService.findAllReservationTimes();

        // then
        assertThat(foundTimes.times()).hasSize(2)
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

    @Test
    @DisplayName("예약이 존재하는 시간을 삭제하면 예외가 발생한다.")
    void deleteReservationTimeBy_throwsException_whenReservationExists() {
        // given
        Long timeId = 1L;
        doThrow(DataIntegrityViolationException.class)
                .when(reservationTimeRepository).deleteById(timeId);

        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTimeBy(timeId))
                .isInstanceOf(ReservationTimeDeleteConflictException.class);
    }
}
