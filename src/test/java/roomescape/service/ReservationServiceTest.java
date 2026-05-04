package roomescape.service;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.controller.dto.ReservationCreateRequest;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeService reservationTimeService;

    @Mock
    private ThemeService themeService;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void 예약_취소_성공() {
        ReservationTime reservationTime = ReservationTime.of("10:00");
        ReservationDate reservationDate = ReservationDate.from("2026-05-03");
        Theme theme = Theme.of(1L, "공포", "무서워요", "https://zeze.com");
        Reservation reservation = Reservation.of(1L, "zeze", reservationDate, reservationTime, theme);

        given(reservationRepository.findById(1L)).willReturn(Optional.of(reservation));

        reservationService.cancel(1L);

        verify(reservationRepository).deleteById(1L);
    }

    @Test
    void 존재하지_않는_예약_취소시_예외_발생() {
        given(reservationRepository.findById(999L)).willReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> reservationService.cancel(999L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_시간으로_예약시_예외() {
        given(reservationTimeService.find(999L)).willThrow(new IllegalArgumentException());

        ReservationCreateRequest request = new ReservationCreateRequest("zeze", "2026-05-03", 999L, 1L);

        Assertions.assertThatThrownBy(() -> reservationService.reserve(request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
