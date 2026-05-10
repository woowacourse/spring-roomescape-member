package roomescape.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
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
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.ReservationTimeCreateCommand;
import roomescape.service.exception.ReservationTimeConflictException;
import roomescape.service.exception.ReservationTimeInUseException;
import roomescape.service.exception.ReservationTimeNotFoundException;
import roomescape.service.exception.ThemeNotFoundException;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    private static final LocalTime VALID_START_AT = LocalTime.of(10, 0);

    @Mock
    private ReservationTimeRepository reservationTimeRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private ThemeRepository themeRepository;
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
    @DisplayName("존재하지 않는 시간을 삭제하면 ReservationTimeNotFoundException이 발생한다")
    void 존재하지_않는_시간_삭제시_예외가_발생한다() {
        given(reservationTimeRepository.existsById(1L)).willReturn(false);

        assertThrows(
                ReservationTimeNotFoundException.class,
                () -> reservationTimeService.delete(1L)
        );
    }

    @Test
    @DisplayName("예약이 존재하는 시간을 삭제하면 ReservationTimeInUseException이 발생한다")
    void 예약이_존재하는_시간_삭제시_예외가_발생한다() {
        given(reservationTimeRepository.existsById(1L)).willReturn(true);
        given(reservationRepository.existsByTimeId(1L)).willReturn(true);

        assertThrows(
                ReservationTimeInUseException.class,
                () -> reservationTimeService.delete(1L)
        );
    }

    @Test
    @DisplayName("예약이 없는 시간은 정상적으로 삭제된다")
    void 예약이_없는_시간은_정상_삭제된다() {
        given(reservationTimeRepository.existsById(1L)).willReturn(true);
        given(reservationRepository.existsByTimeId(1L)).willReturn(false);

        assertDoesNotThrow(() -> reservationTimeService.delete(1L));
    }

    @Test
    @DisplayName("가용 시간 조회 시 존재하지 않는 테마이면 ThemeNotFoundException이 발생한다")
    void 가용_시간_조회시_없는_테마이면_예외가_발생한다() {
        given(themeRepository.existsById(1L)).willReturn(false);

        assertThrows(
                ThemeNotFoundException.class,
                () -> reservationTimeService.findAvailable(LocalDate.of(2026, 5, 9), 1L)
        );
    }

    @Test
    @DisplayName("가용 시간 조회 시 존재하는 테마이면 정상적으로 결과를 반환한다")
    void 가용_시간_조회시_존재하는_테마이면_정상_반환한다() {
        given(themeRepository.existsById(1L)).willReturn(true);
        given(reservationTimeRepository.findAvailable(any(), any())).willReturn(java.util.List.of());

        assertDoesNotThrow(() -> reservationTimeService.findAvailable(LocalDate.of(2026, 5, 9), 1L));
    }
}