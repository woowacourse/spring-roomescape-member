package roomescape.service;

import static org.mockito.BDDMockito.given;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.repository.ReservationTimeRepository;

@ExtendWith(MockitoExtension.class)
public class ReservationTimeServiceTest {
    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @Test
    void 존재하지_않는_시간_삭제시_예외() {
        given(reservationTimeRepository.findById(999L)).willThrow(new IllegalArgumentException());

        Assertions.assertThatThrownBy(() -> reservationTimeService.delete(999L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_시간_조회시_예외() {
        given(reservationTimeRepository.findById(999L)).willThrow(new IllegalArgumentException());

        Assertions.assertThatThrownBy(() -> reservationTimeService.find(999L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
