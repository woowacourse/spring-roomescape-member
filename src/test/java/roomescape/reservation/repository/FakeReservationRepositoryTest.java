package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;

class FakeReservationRepositoryTest {

    private final LocalDate futureDate = LocalDate.now().plusDays(1);
    private FakeReservationRepository repository;

    @BeforeEach
    void setUp() {
        repository = new FakeReservationRepository();
    }

    @Test
    void put_shouldStoreReservation() {
        Reservation reservation = Reservation.of(1L, "브라운", futureDate, ReservationTime.of(1L, "15:40"));

        repository.put(reservation);

        assertThat(repository.getAll()).hasSize(1);
    }

    @Test
    void getAll_shouldReturnAllSavedReservations() {
        Reservation r1 = Reservation.of(1L, "브라운", futureDate, ReservationTime.of(1L, "15:40"));
        Reservation r2 = Reservation.of(1L, "존", futureDate, ReservationTime.of(1L, "16:00"));

        repository.put(r1);
        repository.put(r2);

        List<Reservation> all = repository.getAll();
        assertThat(all).hasSize(2);
    }

    @Test
    void deleteById_shouldRemoveReservation() {
        repository.put(Reservation.of(1L, "브라운", futureDate, ReservationTime.of(1L, "15:40")));

        repository.deleteById(1L);

        assertThat(repository.getAll()).isEmpty();
    }
}