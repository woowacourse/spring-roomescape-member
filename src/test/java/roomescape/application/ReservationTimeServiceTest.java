package roomescape.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.exception.code.ReservationErrorCode;
import roomescape.exception.custom.BusinessException;
import roomescape.reservation.application.ReservationTimeService;
import roomescape.reservation.infra.ReservationRepository;
import roomescape.reservation.infra.ReservationTimeRepository;

@ExtendWith(MockitoExtension.class)
public class ReservationTimeServiceTest {
    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setUp() {
        reservationTimeService = new ReservationTimeService(
                reservationTimeRepository,
                reservationRepository
        );
    }

    @Test
    void 시간_정상_삭제_테스트() {
        Long id = 1L;

        reservationTimeService.delete(id);

        verify(reservationTimeRepository).deleteById(id);
    }

    @Test
    void 이미_예약이_존재하는_시간은_삭제할_수_없다() {
        Long id = 1L;

        doThrow(new DataIntegrityViolationException("외래키 제약"))
                .when(reservationTimeRepository)
                .deleteById(id);

        assertThatThrownBy(() -> reservationTimeService.delete(id))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ReservationErrorCode.RESERVATION_TIME_DELETE_CONFLICT);
    }
}
