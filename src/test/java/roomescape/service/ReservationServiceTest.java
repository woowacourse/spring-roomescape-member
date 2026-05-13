package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.controller.dto.ReservationCreateRequest;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeService reservationTimeService;

    @Mock
    private ThemeService themeService;

    @Test
    void 예약을_생성한다() {
        ReservationCreateRequest request = new ReservationCreateRequest("아이큐", "2025-06-01", 1L, 1L);
        ReservationTime time = ReservationTime.of(1L, "10:00");
        Theme theme = Theme.of(1L, "공포", "desc", "url");
        Reservation saved = Reservation.of(1L, "아이큐", "2025-06-01", time, theme);

        given(reservationTimeService.find(1L)).willReturn(time);
        given(themeService.find(1L)).willReturn(theme);
        given(reservationRepository.findByTimeAndTheme(any(), any())).willReturn(List.of());
        given(reservationRepository.save(any())).willReturn(saved);

        Reservation result = reservationService.reserve(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("아이큐");
        assertThat(result.getTime().getId()).isEqualTo(1L);
        assertThat(result.getTheme().getId()).isEqualTo(1L);
    }

    @Test
    void 중복_예약시_예외가_발생한다() {
        ReservationCreateRequest request = new ReservationCreateRequest("아이큐", "2025-06-01", 1L, 1L);
        ReservationTime time = ReservationTime.of(1L, "10:00");
        Theme theme = Theme.of(1L, "공포", "desc", "url");
        Reservation existing = Reservation.of(1L, "기존유저", "2025-06-01", time, theme);

        given(reservationTimeService.find(1L)).willReturn(time);
        given(themeService.find(1L)).willReturn(theme);
        given(reservationRepository.findByTimeAndTheme(any(), any())).willReturn(List.of(existing));

        assertThatThrownBy(() -> reservationService.reserve(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 예약된 테마의 시간대입니다.");
    }

    @Test
    void 존재하지_않는_예약_취소시_예외가_발생한다() {
        given(reservationRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.cancel(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("요청한 예약을 찾을 수 없습니다.");
    }

    @Test
    void 존재하지_않는_테마로_예약시_예외가_발생한다() {
        ReservationCreateRequest request = new ReservationCreateRequest("아이큐", "2025-06-01", 1L, 999L);
        ReservationTime time = ReservationTime.of(1L, "10:00");

        given(reservationTimeService.find(1L)).willReturn(time);
        given(themeService.find(999L))
                .willThrow(new IllegalArgumentException("존재하지 않는 테마입니다"));

        assertThatThrownBy(() -> reservationService.reserve(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 테마입니다");
    }
}
