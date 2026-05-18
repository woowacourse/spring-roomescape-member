package roomescape.time.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.exception.business.BusinessConflictException;
import roomescape.exception.business.BusinessException;
import roomescape.exception.business.ErrorCode;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.repository.ReservationTimeRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @Test
    void 시간_삭제_시_예약이_존재하는_시간이면_예외가_발생한다() {
        when(reservationRepository.existsByTimeId(anyLong())).thenReturn(true);
        assertThatThrownBy(() -> reservationTimeService.deleteTime(999L))
                .isInstanceOf(BusinessConflictException.class)
                .extracting(e -> ((BusinessException) e).getErrorCode())
                        .isEqualTo(ErrorCode.RESERVATION_TIME_IN_USE);
    }
}
