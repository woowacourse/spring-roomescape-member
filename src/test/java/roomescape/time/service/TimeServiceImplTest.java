package roomescape.time.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import roomescape.error.ErrorCode;
import roomescape.error.RoomescapeException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.exception.TimeNotFoundException;
import roomescape.time.repository.TimeRepository;

@ExtendWith(MockitoExtension.class)
class TimeServiceImplTest {

    @Mock
    private TimeRepository timeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    private TimeServiceImpl timeService;

    @BeforeEach
    void setUp() {
        timeService = new TimeServiceImpl(timeRepository, reservationRepository);
    }

    @Test
    void 시간이_없으면_삭제시_예외가_발생한다() {
        when(timeRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> timeService.deleteById(1L))
                .isInstanceOf(TimeNotFoundException.class);

        verify(reservationRepository, never()).existsByTimeId(1L);
    }

    @Test
    void 예약이_존재하면_삭제시_예외가_발생한다() {
        when(timeRepository.existsById(1L)).thenReturn(true);
        when(reservationRepository.existsByTimeId(1L)).thenReturn(true);

        assertThatThrownBy(() -> timeService.deleteById(1L))
                .isInstanceOf(RoomescapeException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.RESERVED_TIME_DELETE_NOT_ALLOWED);

        verify(timeRepository, never()).deleteById(1L);
    }
}
