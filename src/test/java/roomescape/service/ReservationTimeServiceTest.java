package roomescape.service;

import static org.mockito.BDDMockito.given;

import java.time.LocalTime;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@ExtendWith(MockitoExtension.class)
public class ReservationTimeServiceTest {
    @Mock
    private ReservationTimeRepository reservationTimeRepository;
    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @Test
    void 정상적인_시간_삭제는_성공해야_한다() {
        given(reservationTimeRepository.existsById(1L)).willReturn(true);
        given(reservationRepository.existsByTimeId(1L)).willReturn(false);

        Assertions.assertThatNoException().isThrownBy(() -> reservationTimeService.delete(1L));
    }

    @Test
    void 존재하지_않는_시간_삭제시_예외() {
        given(reservationTimeRepository.existsById(999L)).willReturn(false);

        Assertions.assertThatThrownBy(() -> reservationTimeService.delete(999L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 예약이_있는_시간_삭제시_예외() {
        given(reservationTimeRepository.existsById(1L)).willReturn(true);
        given(reservationRepository.existsByTimeId(1L)).willThrow(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> reservationTimeService.delete(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하는_시간_조회는_성공해야_한다() {
        ReservationTime reservationTime = ReservationTime.of(1L, LocalTime.now());
        given(reservationTimeRepository.findById(1L)).willReturn(Optional.of(reservationTime));

        Assertions.assertThatNoException().isThrownBy(() -> reservationTimeService.find(1L));
    }

    @Test
    void 존재하지_않는_시간_조회시_예외() {
        given(reservationTimeRepository.findById(999L)).willThrow(new IllegalArgumentException());

        Assertions.assertThatThrownBy(() -> reservationTimeService.find(999L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
