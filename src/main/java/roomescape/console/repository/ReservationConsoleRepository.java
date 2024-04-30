package roomescape.console.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.core.domain.Reservation;
import roomescape.core.repository.ReservationRepository;

public class ReservationConsoleRepository implements ReservationRepository {
    private final List<Reservation> reservations;
    private final AtomicLong id;

    public ReservationConsoleRepository() {
        reservations = new ArrayList<>();
        id = new AtomicLong(1);
    }

    @Override
    public Long save(final Reservation reservation) {
        final Reservation persistReservation = new Reservation(
                id.getAndIncrement(),
                reservation.getName(),
                reservation.getDateString(),
                reservation.getReservationTime());
        reservations.add(persistReservation);
        return persistReservation.getId();
    }

    @Override
    public List<Reservation> findAll() {
        return Collections.unmodifiableList(reservations);
    }

    @Override
    public List<Reservation> findByTimeId(final long timeId) {
        return reservations.stream()
                .filter(reservation -> reservation.getTimeId() == timeId)
                .toList();
    }

    @Override
    public Integer countByDateAndTimeId(String date, long timeId) {
        return 0;
    }

    @Override
    public void deleteById(final long id) {
        reservations.removeIf(reservation -> reservation.getId() == id);
    }
}
