package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.ReservationTimeRepository;
import roomescape.theme.doamin.Theme;
import roomescape.theme.repository.ThemeRepository;

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

    @DisplayName("예약을 생성한다.")
    @Test
    void createReservationSuccess() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(2L, "공포 테마", "무서운 방탈출", "https://image.test/theme.png");

        when(reservationTimeRepository.findById(1L)).thenReturn(Optional.of(time));
        when(themeRepository.findById(2L)).thenReturn(Optional.of(theme));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(10L);

        long reservationId = reservationService.createReservation("체셔", LocalDate.of(2026, 5, 10), 1L, 2L);

        assertThat(reservationId).isEqualTo(10L);
        verify(reservationRepository).save(any(Reservation.class));
    }

    @DisplayName("존재하지 않는 예약 시간으로 예약을 생성하면 예외가 발생한다.")
    @Test
    void createReservationFailByMissingTime() {
        when(reservationTimeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.createReservation("체셔", LocalDate.of(2026, 5, 10), 1L, 2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 예약 시간입니다.");

        verify(themeRepository, never()).findById(any(Long.class));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @DisplayName("존재하지 않는 테마로 예약을 생성하면 예외가 발생한다.")
    @Test
    void createReservationFailByMissingTheme() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        when(reservationTimeRepository.findById(1L)).thenReturn(Optional.of(time));
        when(themeRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.createReservation("체셔", LocalDate.of(2026, 5, 10), 1L, 2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 테마입니다.");

        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @DisplayName("아이디로 예약을 조회할 수 있다.")
    @Test
    void getReservationSuccess() {
        Reservation reservation = new Reservation(
                1L,
                "체셔",
                LocalDate.of(2026, 5, 10),
                new ReservationTime(2L, LocalTime.of(12, 0)),
                new Theme(3L, "공포 테마", "무서운 방탈출", "https://image.test/theme.png")
        );
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        Reservation foundReservation = reservationService.getReservation(1L);

        assertThat(foundReservation).isEqualTo(reservation);
    }

    @DisplayName("존재하지 않는 예약 조회 시 예외가 발생한다.")
    @Test
    void getReservationFailByMissingReservation() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.getReservation(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 예약입니다.");
    }

    @DisplayName("사용자 이름으로 예약 목록을 조회한다.")
    @Test
    void getReservationsByUsername() {
        List<Reservation> reservations = List.of(
                new Reservation(
                        1L,
                        "체셔",
                        LocalDate.of(2026, 5, 10),
                        new ReservationTime(2L, LocalTime.of(12, 0)),
                        new Theme(3L, "공포 테마", "무서운 방탈출", "https://image.test/theme.png")
                )
        );
        when(reservationRepository.findAllByName("체셔")).thenReturn(reservations);

        List<Reservation> result = reservationService.getReservationsByUsername("체셔");

        assertThat(result).isEqualTo(reservations);
    }
}
