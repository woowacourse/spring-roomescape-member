package roomescape.dao;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.Reservation;

public class InMemoryReservationDAO implements ReservationDAO {

    private final List<Reservation> reservations;
    private final AtomicLong index = new AtomicLong(1);

    public InMemoryReservationDAO(final List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public List<Reservation> findAll() {
        return Collections.unmodifiableList(reservations);
    }

    @Override
    public boolean existsByDateAndTimeId(final LocalDate date, final long timeId) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.isSameDateTime(date, timeId));
    }

    @Override
    public long insert(final Reservation reservation) {
        Reservation saved = reservation.withId(index.getAndIncrement());
        reservations.add(saved);
        return saved.getId();
    }

    @Override
    public boolean deleteById(final long id) {
        Reservation target = reservations.stream()
                .filter(reservation -> reservation.getId() == id)
                .findAny()
                .orElse(null);
        return reservations.remove(target);
    }
}
