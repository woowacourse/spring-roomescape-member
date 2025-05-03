package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import roomescape.domain.Reservation;

public class FakeReservationRepository implements ReservationRepository {

    private final List<Reservation> reservations;
    private final AtomicLong reservationId = new AtomicLong(1);

    public FakeReservationRepository(final List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public Optional<Reservation> save(final Reservation reservation) {
        Reservation newReservation = new Reservation(reservationId.getAndIncrement(), reservation.name(), reservation.date(), reservation.time(), reservation.theme());
        reservations.add(newReservation);
        return findById(newReservation.id());
    }

    @Override
    public List<Reservation> findAll() {
        return reservations;
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservations.stream()
                .filter(reservation -> Objects.equals(reservation.id(), id))
                .findFirst();
    }

    @Override
    public List<Reservation> findByDateTime(LocalDate date, LocalTime time) {
        return reservations.stream()
                .filter(reservation -> reservation.date().equals(date) && reservation.time().startAt().equals(time))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findByDateAndTheme(LocalDate date, long themeId) {
        return reservations.stream()
                .filter(reservation -> reservation.date().equals(date) && reservation.theme().id().equals(themeId))
                .collect(Collectors.toList());
    }

    @Override
    public int deleteById(long id) {
        Reservation deleteReservation = reservations.stream()
                .filter(reservation -> Objects.equals(reservation.id(), id))
                .findFirst().orElse(null);

        if (deleteReservation != null) {
            int affectedRows = (int) reservations.stream()
                    .filter(reservation -> Objects.equals(reservation.id(), deleteReservation.id()))
                    .count();

            reservations.remove(deleteReservation);
            return affectedRows;
        }
        return 0;
    }
}
