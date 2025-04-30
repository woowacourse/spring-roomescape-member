package roomescape.domain.reservation.repository.fake;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.common.exception.EntityNotFoundException;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.repository.ReservationTimeRepository;

public class FakeReservationTimeRepository implements ReservationTimeRepository {

    private static final Long INITIAL_ID = 1L;

    private final AtomicLong id = new AtomicLong(INITIAL_ID);
    private final Map<Long, ReservationTime> reservationTimes = new ConcurrentHashMap<>();

    @Override
    public List<ReservationTime> findAll() {
        return reservationTimes.values()
                .stream()
                .toList();
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        if (reservationTimes.containsKey(id)) {
            return Optional.of(reservationTimes.get(id));
        }

        throw new EntityNotFoundException("ReservationTime with id " + id + " not found");
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        if (reservationTime.existId() && !reservationTimes.containsKey(reservationTime.getId())) {
            throw new EntityNotFoundException("ReservationTime with id " + reservationTime.getId() + " not found");
        }

        if (reservationTime.existId()) {
            reservationTimes.put(reservationTime.getId(), reservationTime);
            return reservationTime;
        }

        ReservationTime timeWithId = new ReservationTime(id.getAndIncrement(), reservationTime.getStartAt());
        reservationTimes.put(timeWithId.getId(), timeWithId);
        return timeWithId;
    }

    @Override
    public void deleteById(Long id) {
        if (!reservationTimes.containsKey(id)) {
            throw new EntityNotFoundException("ReservationTime with id " + id + " not found");
        }

        reservationTimes.remove(id);
    }

    public void add(ReservationTime reservationTime) {
        reservationTimes.put(reservationTime.getId(), reservationTime);
    }

    public void deleteAll() {
        reservationTimes.clear();
        id.set(INITIAL_ID);
    }
}
