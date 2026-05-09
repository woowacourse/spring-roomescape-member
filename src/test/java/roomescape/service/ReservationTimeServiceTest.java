package roomescape.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.ReservationTimeCreateCommand;
import roomescape.service.exception.ReservationTimeConflictException;
import roomescape.service.exception.ReservationTimeInUseException;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    private static final LocalTime VALID_START_AT = LocalTime.of(10, 0);

    @Mock
    private ReservationTimeRepository reservationTimeRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("같은 startAt 시간이 이미 등록되어 있으면 ReservationTimeConflictException이 발생한다")
    void 같은_시간이_이미_있으면_예외가_발생한다() {
        ReservationTimeCreateCommand command = new ReservationTimeCreateCommand(VALID_START_AT);
        given(reservationTimeRepository.existsByStartAt(VALID_START_AT)).willReturn(true);

        assertThrows(
                ReservationTimeConflictException.class,
                () -> reservationTimeService.create(command)
        );
    }

    @Test
    @DisplayName("같은 시간이 없으면 정상적으로 시간을 생성한다")
    void 같은_시간이_없으면_정상_생성한다() {
        ReservationTimeCreateCommand command = new ReservationTimeCreateCommand(VALID_START_AT);
        given(reservationTimeRepository.existsByStartAt(VALID_START_AT)).willReturn(false);
        given(reservationTimeRepository.save(any(ReservationTime.class)))
                .willReturn(new ReservationTime(1L, VALID_START_AT));

        assertDoesNotThrow(() -> reservationTimeService.create(command));
    }

    @Test
    @DisplayName("예약이 존재하는 시간을 삭제하면 ReservationTimeInUseException이 발생한다")
    void 예약이_존재하는_시간_삭제시_예외가_발생한다() {
        given(reservationRepository.existsByTimeId(1L)).willReturn(true);

        assertThrows(
                ReservationTimeInUseException.class,
                () -> reservationTimeService.delete(1L)
        );
    }

    @Test
    @DisplayName("예약이 없는 시간은 정상적으로 삭제된다")
    void 예약이_없는_시간은_정상_삭제된다() {
        given(reservationRepository.existsByTimeId(1L)).willReturn(false);

        assertDoesNotThrow(() -> reservationTimeService.delete(1L));
    }
}