package roomescape.reservation.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.exception.InvalidReservationTimeException;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

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
    void 예약_목록을_조회하면_Repository_findAllWithTime_결과를_반환한다() {
        List<Reservation> reservations = List.of(
                new Reservation(
                        1L,
                        "브라운",
                        LocalDate.of(2026, 5, 10),
                        new ReservationTime(1L, LocalTime.of(10, 0)),
                        new Theme(1L, "공포방", "무서운방입니다.", "image-url")
                )
        );
        when(reservationRepository.findAllWithTime()).thenReturn(reservations);

        List<Reservation> result = reservationService.getReservations();

        verify(reservationRepository).findAllWithTime();
        assertThat(result).isSameAs(reservations);
    }

    @Test
    void 예약을_생성하면_시간과_테마를_조회하고_예약을_저장한_뒤_예약을_반환한다() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(2L, "공포방", "무서운방입니다.", "image-url");
        Reservation reservation = new Reservation(1L, "어셔", LocalDate.of(2026, 5, 10), time, theme);

        when(reservationTimeRepository.findById(any())).thenReturn(Optional.of(time));
        when(themeRepository.findById(any())).thenReturn(theme);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation result = reservationService.createReservation("어셔", LocalDate.of(2026, 5, 10), 1L, 2L);

        verify(reservationTimeRepository).findById(any());
        verify(themeRepository).findById(any());
        verify(reservationRepository).save(any(Reservation.class));
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("어셔");
        assertThat(result.getDate()).isEqualTo(LocalDate.of(2026, 5, 10));
        assertThat(result.getTime()).isSameAs(time);
        assertThat(result.getTheme()).isSameAs(theme);
    }

    @Test
    void 존재하지_않는_시간으로_예약을_생성하면_예외가_발생하고_예약을_저장하지_않는다() {
        when(reservationTimeRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.createReservation(
                "브라운", LocalDate.of(2026, 5, 10), 999L, 2L))
                .isInstanceOf(InvalidReservationTimeException.class);

        verify(themeRepository, never()).findById(2L);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    void 예약_삭제를_요청하면_Repository_deleteById에_id를_전달한다() {
        when(reservationRepository.deleteById(any())).thenReturn(1);

        reservationService.deleteReservation(7L);

        verify(reservationRepository).deleteById(7L);
    }

    @Test
    void 존재하지_않는_예약을_삭제하면_예외가_발생한다() {
        when(reservationRepository.deleteById(any())).thenReturn(0);

        assertThatThrownBy(() -> reservationService.deleteReservation(999L))
                .isInstanceOf(ReservationNotFoundException.class);
    }
}
