package roomescape.repository.fake;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.model.Reservation;
import roomescape.repository.ReservationRepository;

public class ReservationFakeRepository implements ReservationRepository {

    private final Map<Long, Reservation> reservations = new ConcurrentHashMap<>();
    private final AtomicLong index = new AtomicLong(1L);

    @Override
    public Optional<Reservation> findById(final long id) {
        return Optional.ofNullable(reservations.get(id));
    }

    @Override
    public long save(final Reservation reservation) {
        var id = index.getAndIncrement();
        var created = new Reservation(
            id,
            reservation.name(),
            reservation.date(),
            reservation.timeSlot(),
            reservation.theme()
        );
        reservations.put(id, created);
        return id;
    }

    @Override
    public boolean removeById(final long id) {
        Reservation removed = reservations.remove(id);
        return removed != null;
    }

    @Override
    public List<Reservation> findAll() {
        return List.copyOf(reservations.values());
    }

    @Override
    public List<Reservation> findByTimeSlotId(final long id) {
        return reservations.values().stream()
            .filter(reservation -> reservation.timeSlot().id() == id)
            .toList();
    }

    @Override
    public List<Reservation> findByThemeId(final long id) {
        return reservations.values().stream()
            .filter(reservation -> reservation.theme().id() == id)
            .toList();
    }

    @Override
    public List<Reservation> findByDateAndThemeId(final LocalDate date, final long themeId) {
        return reservations.values()
            .stream()
            .filter(r -> r.date().equals(date) && r.theme().id() == themeId)
            .toList();
    }
}
