package roomescape;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationRepository;

public class FakeReservationRepository implements ReservationRepository {

    private final Map<Long, Reservation> store = new HashMap<>();
    private Long sequence = 0L;

    @Override
    public Reservation save(Reservation reservation) {
        if (reservation.getId() == null) {
            Reservation saved = Reservation.of(
                    sequence++,
                    reservation.getName(),
                    reservation.getDate(),
                    reservation.getTime()
            );
            store.put(saved.getId(), saved);
            return saved;
        }

        store.put(reservation.getId(), reservation);
        return reservation;
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Reservation> findAll() {
        return store.values().stream().toList();
    }

    @Override
    public boolean existsByReservationTimeId(Long reservationTimeId) {
        return store.values().stream()
                .anyMatch(reservation -> reservation.getTime()
                        .getId()
                        .equals(reservationTimeId)
                );
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }
}
