package roomescape.fake;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import roomescape.entity.ReservationTime;
import roomescape.entity.ReservationTimeRepository;

public class FakeReservationTimeRepository implements ReservationTimeRepository {

    private final Map<Long, ReservationTime> store = new HashMap<>();
    private Long sequence = 0L;

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        if (reservationTime.id() == null) {
            ReservationTime saved = ReservationTime.createWithId(
                    sequence++,
                    reservationTime.startAt()
            );
            store.put(saved.id(), saved);
            return saved;
        }
        store.put(reservationTime.id(), reservationTime);
        return reservationTime;
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<ReservationTime> findAll() {
        return store.values().stream().toList();
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }
}
