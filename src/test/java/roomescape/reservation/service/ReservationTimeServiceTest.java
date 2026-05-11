package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.reservation.controller.dto.ReservationTimeResponse;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.ReservationTimeRepository;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @DisplayName("예약시간 목록을 전부 조회할 수 있다.")
    @Test
    void getAllTimes() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        when(reservationTimeRepository.findAll()).thenReturn(List.of(reservationTime));

        List<ReservationTimeResponse> responses = reservationTimeService.getAllTimes();

        assertThat(responses).hasSize(1);
        assertThat(responses.getFirst().id()).isEqualTo(1L);
        assertThat(responses.getFirst().startAt()).isEqualTo("10:00");
    }

    @DisplayName("존재하지 않은 예약 시간을 생성할 수 있다.")
    @Test
    void createTimeSuccess() {
        LocalTime startAt = LocalTime.of(11, 0);
        when(reservationTimeRepository.existsByStartAt(startAt)).thenReturn(false);
        when(reservationTimeRepository.save(any(ReservationTime.class))).thenReturn(3L);

        long createdId = reservationTimeService.createTime(startAt);

        assertThat(createdId).isEqualTo(3L);
    }

    @DisplayName("이미 존재하는 예약 시간은 생성할 수 없다.")
    @Test
    void createTimeFailByDuplicatedTime() {
        LocalTime startAt = LocalTime.of(11, 0);
        when(reservationTimeRepository.existsByStartAt(startAt)).thenReturn(true);

        assertThatThrownBy(() -> reservationTimeService.createTime(startAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 예약 시간입니다.");
    }

    @DisplayName("예약에 사용 중인 시간은 삭제할 수 없다.")
    @Test
    void deleteTimeFailByInUse() {
        when(reservationRepository.existsByTimeId(1L)).thenReturn(true);

        assertThatThrownBy(() -> reservationTimeService.deleteTime(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약에 사용 중인 시간은 삭제할 수 없습니다.");

        verify(reservationTimeRepository, never()).delete(1L);
    }

    @DisplayName("예약에 사용되지 않은 시간은 삭제할 수 있다.")
    @Test
    void deleteTimeSuccess() {
        when(reservationRepository.existsByTimeId(1L)).thenReturn(false);

        reservationTimeService.deleteTime(1L);

        verify(reservationTimeRepository).delete(1L);
    }

    @DisplayName("아이디로 예약 시간을 조회할 수 있다.")
    @Test
    void getTimeSuccess() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        when(reservationTimeRepository.findById(1L)).thenReturn(Optional.of(reservationTime));

        ReservationTime result = reservationTimeService.getTime(1L);

        assertThat(result).isEqualTo(reservationTime);
    }

    @DisplayName("존재하지 않는 예약 시간 조회 시 예외가 발생한다.")
    @Test
    void getTimeFailByMissingTime() {
        when(reservationTimeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationTimeService.getTime(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 예약 시간입니다.");
    }
}
