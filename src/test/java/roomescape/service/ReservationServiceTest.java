package roomescape.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.command.ReservationSaveCommand;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    private static final long TIME_ID = 1L;
    private static final long THEME_ID = 1L;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void 예약을_저장하면_id가_채워진_도메인을_반환한다() {
        ReservationTime time = new ReservationTime(TIME_ID, LocalTime.of(10, 0));
        Theme theme = new Theme(THEME_ID, "우주 정거장", "설명", "https://example.com/1.jpg");
        ReservationSaveCommand saveCommand = new ReservationSaveCommand("브라운", LocalDate.of(2026, 5, 3), TIME_ID, THEME_ID);
        Reservation persisted = new Reservation(99L, "브라운", LocalDate.of(2026, 5, 3), time, theme);

        given(reservationTimeRepository.findById(TIME_ID)).willReturn(Optional.of(time));
        given(themeRepository.findById(THEME_ID)).willReturn(Optional.of(theme));
        given(reservationRepository.addReservation(any(Reservation.class))).willReturn(persisted);

        Reservation saved = reservationService.saveReservation(saveCommand);

        assertThat(saved.id()).isEqualTo(99L);
        assertThat(saved.name()).isEqualTo("브라운");
        assertThat(saved.date()).isEqualTo(LocalDate.of(2026, 5, 3));
        assertThat(saved.time().id()).isEqualTo(TIME_ID);
        assertThat(saved.theme().id()).isEqualTo(THEME_ID);
    }

    @Test
    void 사용자는_지난_날짜로_예약을_저장할_수_없다() {
        ReservationSaveCommand saveCommand = new ReservationSaveCommand("브라운", LocalDate.now().minusDays(1), TIME_ID, THEME_ID);

        assertThatThrownBy(() -> reservationService.saveUserReservation(saveCommand))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 관리자는_지난_날짜로도_예약을_저장할_수_있다() {
        ReservationTime time = new ReservationTime(TIME_ID, LocalTime.of(10, 0));
        Theme theme = new Theme(THEME_ID, "우주 정거장", "설명", "https://example.com/1.jpg");
        LocalDate yesterday = LocalDate.now().minusDays(1);
        ReservationSaveCommand saveCommand = new ReservationSaveCommand("브라운", yesterday, TIME_ID, THEME_ID);
        Reservation persisted = new Reservation(99L, "브라운", yesterday, time, theme);

        given(reservationTimeRepository.findById(TIME_ID)).willReturn(Optional.of(time));
        given(themeRepository.findById(THEME_ID)).willReturn(Optional.of(theme));
        given(reservationRepository.addReservation(any(Reservation.class))).willReturn(persisted);

        Reservation saved = reservationService.saveReservation(saveCommand);

        assertThat(saved.date()).isEqualTo(yesterday);
    }

    @Test
    void 존재하지_않는_시간으로_저장하면_예외가_발생한다() {
        ReservationSaveCommand saveCommand = new ReservationSaveCommand("브라운", LocalDate.of(2026, 5, 3), TIME_ID, THEME_ID);
        given(reservationTimeRepository.findById(TIME_ID)).willReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.saveReservation(saveCommand))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 존재하지_않는_테마로_저장하면_예외가_발생한다() {
        ReservationTime time = new ReservationTime(TIME_ID, LocalTime.of(10, 0));
        ReservationSaveCommand saveCommand = new ReservationSaveCommand("브라운", LocalDate.of(2026, 5, 3), TIME_ID, THEME_ID);
        given(reservationTimeRepository.findById(TIME_ID)).willReturn(Optional.of(time));
        given(themeRepository.findById(THEME_ID)).willReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.saveReservation(saveCommand))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 저장된_모든_예약을_조회한다() {
        ReservationTime time = new ReservationTime(TIME_ID, LocalTime.of(10, 0));
        Theme theme = new Theme(THEME_ID, "우주 정거장", "설명", "https://example.com/1.jpg");
        List<Reservation> stored = List.of(
                new Reservation(1L, "브라운", LocalDate.of(2026, 5, 3), time, theme),
                new Reservation(2L, "조이", LocalDate.of(2026, 5, 4), time, theme));
        given(reservationRepository.findAllReservations()).willReturn(stored);

        List<Reservation> reservations = reservationService.findAllReservations();

        assertThat(reservations).hasSize(2);
    }

    @Test
    void 사용자명이_없으면_NotFound_예외() {
        given(reservationRepository.findReservationsByName(any())).willReturn(List.of());
        assertThatThrownBy(() -> reservationService.findReservationsByName(" "))
                .isInstanceOf(NotFoundException.class);
        verify(reservationRepository, times(1)).findReservationsByName(any());
    }

    @Test
    void 예약이_없으면_빈_리스트를_반환한다() {
        given(reservationRepository.findAllReservations()).willReturn(List.of());

        List<Reservation> reservations = reservationService.findAllReservations();

        assertThat(reservations).isEmpty();
    }

    @Test
    void id로_예약을_삭제한다() {
        long reservationId = 1L;

        reservationService.deleteById(reservationId);

        verify(reservationRepository).deleteById(eq(reservationId));
    }
}
