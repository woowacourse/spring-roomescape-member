package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.FakeThemeRepository;
import roomescape.theme.repository.ThemeRepository;

class FakeReservationRepositoryTest {

    private final LocalDate futureDate = LocalDate.now().plusDays(1);
    private ReservationRepository reservationRepository;
    private ThemeRepository themeRepository;
    private Reservation reservation;
    private Theme theme;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        themeRepository = new FakeThemeRepository();

        theme = Theme.of(1L, "추리", "셜록 추리 게임 with Danny", "image.png");
        reservation = Reservation.of(1L, "브라운", futureDate, ReservationTime.of(1L, "15:40"),
                theme);

        themeRepository.put(theme);
    }

    @Test
    void put_shouldStoreReservation() {
        reservationRepository.put(reservation);

        assertThat(reservationRepository.getAll()).hasSize(1);
    }

    @Test
    void getAll_shouldReturnAllSavedReservations() {
        Reservation reservation2 = Reservation.of(1L, "대니", futureDate, ReservationTime.of(1L, "16:00"),
                theme);

        reservationRepository.put(reservation);
        reservationRepository.put(reservation2);

        List<Reservation> all = reservationRepository.getAll();
        assertThat(all).hasSize(2);
    }

    @Test
    void deleteById_shouldRemoveReservation() {
        reservationRepository.deleteById(1L);

        assertThat(reservationRepository.getAll()).isEmpty();
    }
}
