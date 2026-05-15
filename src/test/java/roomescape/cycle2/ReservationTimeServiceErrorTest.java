package roomescape.cycle2;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.exception.CustomException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.ReservationTimeService;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceErrorTest {

    private static final Long TIME_ID = 1L;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @Test
    void 예약이_존재하는_시간_삭제_시_예외() {
        when(reservationRepository.existsByTimeId(TIME_ID)).thenReturn(true);

        assertThatThrownBy(() -> reservationTimeService.delete(TIME_ID))
                .isInstanceOf(CustomException.class)
                .hasMessage("해당 시간에 예약이 존재하여 삭제할 수 없습니다.");
    }
}
