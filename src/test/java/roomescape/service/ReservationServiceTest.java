package roomescape.service;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.controller.dto.ReservationCreateRequest;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void 예약_취소_성공() {
        given(reservationRepository.existsById(1L)).willReturn(true);

        reservationService.cancel(1L);

        verify(reservationRepository).deleteById(1L);
    }

    @Test
    void 존재하지_않는_예약_취소시_예외_발생() {
        given(reservationRepository.existsById(999L)).willReturn(false);

        Assertions.assertThatThrownBy(() -> reservationService.cancel(999L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_시간으로_예약시_예외() {
        given(reservationTimeRepository.findById(999L)).willReturn(Optional.empty());

        ReservationCreateRequest request = new ReservationCreateRequest("zeze", LocalDate.parse("2026-05-03"), 999L,
                1L);

        Assertions.assertThatThrownBy(() -> reservationService.reserve(request, LocalDateTime.MAX))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 지나간_날짜로_예약_시_예외가_발생해야_한다() {
        ReservationTime reservationTime = ReservationTime.of(LocalTime.parse("11:00"));
        Theme theme = Theme.of(1L, "테마1", "설명", "URL");

        ReservationCreateRequest request = new ReservationCreateRequest("zeze", LocalDate.parse("2026-04-05"), 1L, 1L);
        given(reservationTimeRepository.findById(1L)).willReturn(Optional.of(reservationTime));
        given(themeRepository.findById(1L)).willReturn(Optional.of(theme));

        Assertions.assertThatThrownBy(() -> reservationService.reserve(request, LocalDateTime.MAX));
    }

    @Test
    void 같은_날짜이며_시간이_1초_전이면_예약에_성공해야_한다() {
        ReservationTime reservationTime = ReservationTime.of(LocalTime.parse("11:00"));
        Theme theme = Theme.of(1L, "테마1", "설명", "URL");

        ReservationCreateRequest request = new ReservationCreateRequest("zeze", LocalDate.parse("2026-04-05"), 1L, 1L);
        given(reservationTimeRepository.findById(1L)).willReturn(Optional.of(reservationTime));
        given(themeRepository.findById(1L)).willReturn(Optional.of(theme));

        Assertions.assertThatNoException()
                .isThrownBy(() -> reservationService.reserve(request, LocalDateTime.of(2026, 4, 5, 10, 59, 59)));
    }

    @Test
    void 같은_날짜이며_시간이_1초_지났다면_예약에_실패해야_한다() {
        ReservationTime reservationTime = ReservationTime.of(LocalTime.parse("11:00"));
        Theme theme = Theme.of(1L, "테마1", "설명", "URL");

        ReservationCreateRequest request = new ReservationCreateRequest("zeze", LocalDate.parse("2026-04-05"), 1L, 1L);
        given(reservationTimeRepository.findById(1L)).willReturn(Optional.of(reservationTime));
        given(themeRepository.findById(1L)).willReturn(Optional.of(theme));

        Assertions.assertThatThrownBy(
                () -> reservationService.reserve(request, LocalDateTime.of(2026, 4, 5, 11, 0, 1)));
    }

    @Test
    void 미래로_예약하면_성공해야_한다() {
        ReservationTime reservationTime = ReservationTime.of(LocalTime.parse("11:00"));
        Theme theme = Theme.of(1L, "테마1", "설명", "URL");

        ReservationCreateRequest request = new ReservationCreateRequest("zeze", LocalDate.parse("2026-04-06"), 1L, 1L);
        given(reservationTimeRepository.findById(1L)).willReturn(Optional.of(reservationTime));
        given(themeRepository.findById(1L)).willReturn(Optional.of(theme));

        Assertions.assertThatNoException().isThrownBy(
                () -> reservationService.reserve(request, LocalDateTime.of(2026, 4, 5, 11, 0, 1)));

    }
}
