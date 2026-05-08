package roomescape.reservation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.exception.ApiException;
import roomescape.exception.DuplicateException;
import roomescape.exception.NotFoundException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private UserReservationService userReservationService;

    private Theme theme;
    private ReservationTime time;

    @BeforeEach
    void setUp() {
        theme = new Theme(1L, "Theme A", "desc", "https://example.com/a.png");
        time = new ReservationTime(2L, LocalTime.of(10, 0));
    }

    @Test
    void 예약을_등록할_수_있다() {
        Reservation savedReservation = new Reservation(3L, "브라운", LocalDate.of(2026, 5, 1), time, theme);

        when(reservationTimeRepository.findById(eq(time.id()))).thenReturn(Optional.of(time));
        when(themeRepository.findById(eq(theme.id()))).thenReturn(Optional.of(theme));
        when(reservationRepository.save(eq("브라운"), eq(LocalDate.of(2026, 5, 1)), eq(time), eq(theme)))
                .thenReturn(savedReservation);

        Reservation saved = userReservationService.createReservation("브라운", LocalDate.of(2026, 5, 1), time.id(),
                theme.id());

        assertThat(saved.getName()).isEqualTo("브라운");
        assertThat(saved.getDate()).isEqualTo(LocalDate.of(2026, 5, 1));
        assertThat(saved.getTime().startAt()).isEqualTo(LocalTime.of(10, 0));
        assertThat(saved.getTheme().name()).isEqualTo("Theme A");
    }

    @Test
    void 예약_시간_ID가_없으면_예외가_발생한다() {
        when(reservationTimeRepository.findById(eq(999L))).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> userReservationService.createReservation("브라운", LocalDate.of(2026, 5, 1), 999L, theme.id()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 테마_ID가_없으면_예외가_발생한다() {
        when(reservationTimeRepository.findById(eq(time.id()))).thenReturn(Optional.of(time));
        when(themeRepository.findById(eq(999L))).thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> userReservationService.createReservation("브라운", LocalDate.of(2026, 5, 1), time.id(), 999L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 예약이_중복되면_예외가_발생한다() {
        when(reservationTimeRepository.findById(eq(time.id()))).thenReturn(Optional.of(time));
        when(themeRepository.findById(eq(theme.id()))).thenReturn(Optional.of(theme));
        when(reservationRepository.save(any(), any(), eq(time), eq(theme)))
                .thenThrow(new DuplicateException("해당 날짜의 해당 시간은 이미 예약되었습니다"));

        assertThatThrownBy(
                () -> userReservationService.createReservation("코니", LocalDate.of(2026, 5, 1), time.id(), theme.id()))
                .isInstanceOf(DuplicateException.class)
                .extracting(Throwable::getMessage)
                .isEqualTo("해당 날짜의 해당 시간은 이미 예약되었습니다");
    }

    @Test
    void 예약을_삭제할_수_있다() {
        Reservation savedReservation = new Reservation(3L, "브라운", LocalDate.of(2026, 5, 1), time, theme);
        when(reservationRepository.findById(eq(3L))).thenReturn(Optional.of(savedReservation));

        userReservationService.deleteReservation(3L, "브라운");

        verify(reservationRepository).delete(eq(3L));
    }

    @Test
    void 예약자_이름이_다르면_예외가_발생한다() {
        Reservation savedReservation = new Reservation(3L, "브라운", LocalDate.of(2026, 5, 1), time, theme);

        when(reservationRepository.findById(eq(3L))).thenReturn(Optional.of(savedReservation));

        assertThatThrownBy(() -> userReservationService.deleteReservation(3L, "코니"))
                .isInstanceOf(ApiException.class)
                .extracting(Throwable::getMessage)
                .isEqualTo("예약자 이름이 일치하지 않습니다.");
    }
}
