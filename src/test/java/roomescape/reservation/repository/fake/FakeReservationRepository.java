package roomescape.reservation.repository.fake;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.common.exception.EntityNotFoundException;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.repository.ReservationRepository;

public class FakeReservationRepository implements ReservationRepository {

    private static final Long INITIAL_ID = 1L;

    private final AtomicLong id = new AtomicLong(INITIAL_ID);
    private final Map<Long, Reservation> reservations = new ConcurrentHashMap<>();

    @Override
    public List<Reservation> findAll() {
        return reservations.values()
                .stream()
                .toList();
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        if (reservations.containsKey(id)) {
            return Optional.ofNullable(reservations.get(id));
        }

        throw new EntityNotFoundException("Reservation with id " + id + " not found");
    }

    @Override
    public Reservation save(Reservation reservation) {
        if (reservation.existId() && !reservations.containsKey(reservation.getId())) {
            throw new EntityNotFoundException("Reservation with id " + reservation.getId() + " not found");
        }

        if (reservation.existId()) {
            reservations.put(reservation.getId(), reservation);
            return reservation;
        }

        Reservation reservationWithId = new Reservation(id.getAndIncrement(), reservation.getName(),
                reservation.getReservationDate(), reservation.getReservationTime());

        reservations.put(reservationWithId.getId(), reservationWithId);
        return reservationWithId;
    }

    @Override
    public void deleteById(Long id) {
        if (!reservations.containsKey(id)) {
            throw new EntityNotFoundException("Reservation with id " + id + " not found");
        }

        reservations.remove(id);
    }

    @Override
    public boolean existsByDateAndTimeId(LocalDate date, Long timeId) {
        return reservations.values()
                .stream()
                .anyMatch(reservation -> sameReservationDateTime(reservation, date, timeId));
    }

    private boolean sameReservationDateTime(Reservation reservation, LocalDate date, Long timeId) {
        return reservation.getReservationDate().equals(date) &&
               reservation.getReservationTime().getId().equals(timeId);
    }

    public void deleteAll() {
        reservations.clear();
        id.set(INITIAL_ID);
    }
}
