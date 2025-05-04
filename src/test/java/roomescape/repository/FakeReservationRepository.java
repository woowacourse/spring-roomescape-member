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
        Reservation newReservation = new Reservation(reservationId.getAndIncrement(), reservation.getName(), reservation.getDate(), reservation.getTime(), reservation.getTheme());
        reservations.add(newReservation);
        return findById(newReservation.getId());
    }

    @Override
    public List<Reservation> findAll() {
        return reservations;
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservations.stream()
                .filter(reservation -> Objects.equals(reservation.getId(), id))
                .findFirst();
    }

    @Override
    public List<Reservation> findByDateTimeTheme(LocalDate date, LocalTime time, long themeId) {
        return reservations.stream()
                .filter(reservation -> reservation.getDate().equals(date) && reservation.getTime().getStartAt().equals(time) && reservation.getTheme().getId().equals(themeId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reservation> findByDateAndTheme(LocalDate date, long themeId) {
        return reservations.stream()
                .filter(reservation -> reservation.getDate().equals(date) && reservation.getTheme().getId().equals(themeId))
                .collect(Collectors.toList());
    }

    @Override
    public int deleteById(long id) {
        Reservation deleteReservation = reservations.stream()
                .filter(reservation -> Objects.equals(reservation.getId(), id))
                .findFirst().orElse(null);

        if (deleteReservation != null) {
            int affectedRows = (int) reservations.stream()
                    .filter(reservation -> Objects.equals(reservation.getId(), deleteReservation.getId()))
                    .count();

            reservations.remove(deleteReservation);
            return affectedRows;
        }
        return 0;
    }
}
