package roomescape.reservation.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.exception.EntityNotFoundException;

@Repository
public class ReservationRepositoryImpl implements ReservationRepository {

    private static final int INIT_INDEX = 1;

    private final Map<Integer, Reservation> database = new TreeMap<>();
    private final AtomicLong index = new AtomicLong(INIT_INDEX);

    @Override
    public long generateId() {
        return index.getAndIncrement();
    }

    @Override
    public List<Reservation> findAll() {
        return database.values().stream()
                .toList();
    }

    @Override
    public Optional<Reservation> findById(long id) {
        return Optional.ofNullable(database.get((int) id));
    }

    @Override
    public Reservation save(Reservation reservation) {
        database.put((int) reservation.getId(), reservation);

        return reservation;
    }

    @Override
    public void deleteById(long id) {
        if (!database.containsKey((int) id)) {
            throw new EntityNotFoundException("해당 엔티티가 존재하지 않습니다. id = " + id);
        }

        database.remove((int) id);
    }
}
