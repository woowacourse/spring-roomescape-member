package roomescape.reservation.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.repository.ReservationTimeRepository;

public class FakeReservationTimeDao implements ReservationTimeRepository {
    private final Map<Long, ReservationTime> reservationTimes = new HashMap<>();

    @Override
    public ReservationTime save(final ReservationTime reservationTime) {
        reservationTimes.put((long) reservationTimes.size() + 1, reservationTime);
        return new ReservationTime((long) reservationTimes.size(), reservationTime.getStartAt());
    }

    @Override
    public List<ReservationTime> findAll() {
        return reservationTimes.values()
                .stream()
                .toList();
    }

    @Override
    public Optional<ReservationTime> findById(final long timeId) {
        return Optional.of(reservationTimes.get(timeId));
    }

    @Override
    public boolean deleteById(final long timeId) {
        if (!reservationTimes.containsKey(timeId)) {
            return false;
        }
        reservationTimes.remove(timeId);
        return true;
    }
}
