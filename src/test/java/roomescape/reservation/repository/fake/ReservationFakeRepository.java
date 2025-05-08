package roomescape.reservation.repository.fake;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;

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
    public Optional<Reservation> findById(Long reservationId) {
        return Optional.ofNullable(reservations.get(reservationId));
    }

    @Override
    public List<Reservation> findAll() {
        return reservations.values().stream().toList();
    }

    @Override
    public List<Reservation> findAllByTimeId(Long timeId) {
        return reservations.values().stream()
                .filter(reservation -> reservation.getTimeId().equals(timeId))
                .toList();
    }

    @Override
    public List<Reservation> findAllByThemeId(Long themeId) {
        return reservations.values().stream()
                .filter(reservation -> reservation.getThemeId().equals(themeId))
                .toList();
    }

}
