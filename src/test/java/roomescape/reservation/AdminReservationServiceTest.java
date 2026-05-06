package roomescape.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.exception.NotFoundException;
import roomescape.reservationtime.ReservationTime;
import roomescape.reservationtime.ReservationTimeRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;

@ExtendWith(MockitoExtension.class)
class AdminReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private AdminReservationService adminReservationService;

    @Test
    void 관리자가_예약을_등록할_수_있다() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(2L, "Theme A", "desc", "thumb");
        Reservation saved = new Reservation(3L, "브라운", LocalDate.of(2026, 5, 1), time, theme);

        when(reservationTimeRepository.findById(eq(1L))).thenReturn(Optional.of(time));
        when(themeRepository.findById(eq(2L))).thenReturn(Optional.of(theme));
        when(reservationRepository.save(eq(theme), eq("브라운"), eq(LocalDate.of(2026, 5, 1)), eq(time)))
                .thenReturn(saved);

        Reservation result = adminReservationService.createForceReservation(2L, "브라운", LocalDate.of(2026, 5, 1), 1L);

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo("브라운");
        assertThat(result.getTime().startAt()).isEqualTo(LocalTime.of(10, 0));
        assertThat(result.getTheme().name()).isEqualTo("Theme A");
    }

    @Test
    void 예약_시간이_없으면_예외가_발생한다() {
        when(reservationTimeRepository.findById(eq(999L))).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> adminReservationService.createForceReservation(2L, "브라운", LocalDate.of(2026, 5, 1), 999L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 테마가_없으면_예외가_발생한다() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        when(reservationTimeRepository.findById(eq(1L))).thenReturn(Optional.of(time));
        when(themeRepository.findById(eq(999L))).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> adminReservationService.createForceReservation(999L, "브라운", LocalDate.of(2026, 5, 1), 1L))
                .isInstanceOf(NotFoundException.class);
    }
}
