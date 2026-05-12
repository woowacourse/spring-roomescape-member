package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.command.ReservationSaveCommand;

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
    @DisplayName("예약을 저장하면 id가 포함된 예약을 반환한다")
    void saveReservation() {
        ReservationTime time = new ReservationTime(TIME_ID, LocalTime.of(10, 0));
        Theme theme = new Theme(THEME_ID, "우주 정거장", "설명", "https://example.com/1.jpg");
        LocalDate date = LocalDate.now().plusDays(1);
        ReservationSaveCommand saveCommand = new ReservationSaveCommand("브라운", date, TIME_ID, THEME_ID);
        Reservation persisted = new Reservation(99L, "브라운", date, time, theme);

        given(reservationTimeRepository.findById(TIME_ID)).willReturn(Optional.of(time));
        given(themeRepository.findById(THEME_ID)).willReturn(Optional.of(theme));
        given(reservationRepository.addReservation(any(Reservation.class))).willReturn(persisted);

        Reservation saved = reservationService.saveReservation(saveCommand);

        assertThat(saved.id()).isEqualTo(99L);
        assertThat(saved.name()).isEqualTo("브라운");
        assertThat(saved.date()).isEqualTo(date);
        assertThat(saved.time().id()).isEqualTo(TIME_ID);
        assertThat(saved.theme().id()).isEqualTo(THEME_ID);
    }

    @Test
    @DisplayName("존재하지 않는 시간으로 예약하면 예외가 발생한다")
    void throwException_WhenTimeNotFound() {
        ReservationSaveCommand saveCommand = new ReservationSaveCommand("브라운", LocalDate.now().plusDays(1), TIME_ID,
                THEME_ID);
        given(reservationTimeRepository.findById(TIME_ID)).willReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.saveReservation(saveCommand))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 테마로 예약하면 예외가 발생한다")
    void throwException_WhenThemeNotFound() {
        ReservationTime time = new ReservationTime(TIME_ID, LocalTime.of(10, 0));
        ReservationSaveCommand saveCommand = new ReservationSaveCommand("브라운", LocalDate.now().plusDays(1), TIME_ID,
                THEME_ID);
        given(reservationTimeRepository.findById(TIME_ID)).willReturn(Optional.of(time));
        given(themeRepository.findById(THEME_ID)).willReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.saveReservation(saveCommand))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("저장된 모든 예약을 조회한다")
    void findAllReservations() {
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
    @DisplayName("사용자명에 해당하는 예약이 없으면 예외가 발생한다")
    void throwException_WhenNoReservationsByName() {
        given(reservationRepository.findReservationsByName(any())).willReturn(List.of());
        assertThatThrownBy(() -> reservationService.findReservationsByName(" "))
                .isInstanceOf(IllegalArgumentException.class);
        verify(reservationRepository, times(1)).findReservationsByName(any());
    }

    @Test
    @DisplayName("저장된 예약이 없으면 빈 목록을 반환한다")
    void findEmptyReservations() {
        given(reservationRepository.findAllReservations()).willReturn(List.of());

        List<Reservation> reservations = reservationService.findAllReservations();

        assertThat(reservations).isEmpty();
    }

    @Test
    @DisplayName("id로 예약을 삭제한다")
    void deleteReservationById() {
        long reservationId = 1L;

        reservationService.deleteById(reservationId);

        verify(reservationRepository).deleteById(eq(reservationId));
    }

    @Test
    @DisplayName("지난 날짜에 대한 예약 생성 시 예외가 발생한다")
    void throwException_WhenPastDate() {
        ReservationTime time = new ReservationTime(TIME_ID, LocalTime.of(10, 0));
        ReservationSaveCommand saveCommand = new ReservationSaveCommand(
                "브라운",
                LocalDate.now().minusDays(1),
                TIME_ID,
                THEME_ID
        );

        given(reservationTimeRepository.findById(TIME_ID)).willReturn(Optional.of(time));

        assertThatThrownBy(() -> reservationService.saveReservation(saveCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지난 날짜와 시간은 예약할 수 없습니다.");
    }

    @Test
    @DisplayName("오늘의 지난 시간에 대한 예약 생성 시 예외가 발생한다")
    void throwException_WhenPastTimeToday() {
        ReservationTime time = new ReservationTime(TIME_ID, LocalTime.now().minusMinutes(1));
        ReservationSaveCommand saveCommand = new ReservationSaveCommand(
                "브라운",
                LocalDate.now(),
                TIME_ID,
                THEME_ID
        );

        given(reservationTimeRepository.findById(TIME_ID)).willReturn(Optional.of(time));

        assertThatThrownBy(() -> reservationService.saveReservation(saveCommand))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지난 날짜와 시간은 예약할 수 없습니다.");
    }
}
