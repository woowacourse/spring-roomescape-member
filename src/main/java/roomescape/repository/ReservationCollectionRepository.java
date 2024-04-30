package roomescape.repository;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ReservationCollectionRepository implements ReservationRepository {
    private final Map<Long, Reservation> reservations = new ConcurrentHashMap<>();
    private final AtomicLong index = new AtomicLong(1);

    @Override
    public Reservation save(Reservation reservation) {
        Reservation newReservation = new Reservation(index.getAndIncrement(), reservation);
        reservations.put(newReservation.getId(), newReservation);
        return reservations.get(newReservation.getId());
    }

    @Override
    public List<Reservation> findAllByDateAndTime(LocalDate date, ReservationTime time) {
        return reservations.values()
                .stream()
                .filter(reservation -> reservation.hasSameDateTime(date, time))
                .toList();
    }

    @Override
    public List<Reservation> findAll() {
        return new ArrayList<>(reservations.values());
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(reservations.get(id));
    }

    @Override
    public void deleteById(Long id) {
        reservations.remove(id);
    }
}
