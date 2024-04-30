package roomescape.application.london;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.application.ReservationTimeService;
import roomescape.application.dto.ReservationTimeCreationRequest;
import roomescape.domain.time.ReservationTime;
import roomescape.domain.time.repository.ReservationTimeRepository;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {
    @InjectMocks
    private ReservationTimeService reservationTimeService;
    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    void 예약_시간을_성공적으로_등록한다() {
        LocalTime startAt = LocalTime.of(13, 0);
        when(reservationTimeRepository.existsByStartAt(any())).thenReturn(false);
        when(reservationTimeRepository.save(any())).thenReturn(new ReservationTime(1L, startAt));
        ReservationTimeCreationRequest request = new ReservationTimeCreationRequest(startAt);

        ReservationTime reservationTime = reservationTimeService.register(request);

        assertThat(reservationTime.getStartAt()).isEqualTo(startAt);
    }

    @Test
    void 중복된_예약_시간이_있으면_등록을_실패한다() {
        when(reservationTimeRepository.existsByStartAt(any())).thenReturn(true);
        LocalTime startAt = LocalTime.of(13, 0);
        ReservationTimeCreationRequest request = new ReservationTimeCreationRequest(startAt);

        assertThatThrownBy(() -> reservationTimeService.register(request))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 존재하는 예약 시간입니다.");
    }

    @Test
    void 예약_시간을_삭제한다() {
        LocalTime startAt = LocalTime.of(13, 0);
        ReservationTime reservationTime = new ReservationTime(1L, startAt);
        when(reservationTimeRepository.findById(anyLong())).thenReturn(Optional.of(reservationTime));

        reservationTimeService.delete(reservationTime.getId());

        verify(reservationTimeRepository).deleteById(reservationTime.getId());
    }

    @Test
    void 존재하지_않는_예약_시간을_삭제하면_예외가_발생한다() {
        when(reservationTimeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationTimeService.delete(1L))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 예약 시간입니다.");
    }
}
