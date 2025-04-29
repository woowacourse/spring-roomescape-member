package roomescape.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.repository.ReservationRepository;

public class ReservationFakeRepository implements ReservationRepository {

    private final Map<Long, Reservation> reservations = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Long saveAndReturnId(Reservation reservation) {
        Long id = idGenerator.incrementAndGet();
        reservations.put(id, reservation.withId(id));
        return id;
    }

    @Override
    public int deleteById(Long id) {
        if (reservations.containsKey(id)) {
            reservations.remove(id);
            return 1;
        }
        return 0;
    }

    @Override
    public List<Reservation> findAll() {
        return reservations.values().stream().toList();
    }

    @Override
    public List<Reservation> findAllByTimeId(Long timeId) {
        return reservations.values().stream()
                .filter(reservation -> reservation.getTime().getId() == timeId)
                .toList();
    }

    @Override
    public Boolean existByDateAndTimeId(LocalDate date, Long timeId) {
        return reservations.values().stream()
                .anyMatch(reservation -> reservation.getDate().equals(date) && reservation.getTime().getId().equals(timeId));
    }
}
