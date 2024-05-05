package roomescape.service.fakeDao;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeDao;

public class FakeReservationTimeDao implements ReservationTimeDao {

    public Map<Long, ReservationTime> reservationTimes;
    AtomicLong atomicLong = new AtomicLong(0);

    public FakeReservationTimeDao() {
        this.reservationTimes = new HashMap<>();
    }

    public FakeReservationTimeDao(List<ReservationTime> reservationsTimes) {
        this.reservationTimes = new HashMap<>();
        for (int i = 0; i < reservationsTimes.size(); i++) {
            this.reservationTimes.put(reservationsTimes.get(i).getId(), reservationsTimes.get(i));
        }
    }

    @Override
    public List<ReservationTime> findAll() {
        return reservationTimes.values().stream().toList();
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        if (reservationTimes.containsKey(id)) {
            return Optional.of(reservationTimes.get(id));
        }
        return Optional.empty();
    }

    @Override
    public boolean existByStartAt(LocalTime startAt) {
        return reservationTimes.values().stream()
                .anyMatch((reservationTime -> reservationTime.getStartAt().equals(startAt)));
    }

    @Override
    public ReservationTime insert(ReservationTime reservationTime) {
        Long id = atomicLong.incrementAndGet();
        ReservationTime addReservationTime = new ReservationTime(id, reservationTime.getStartAt());
        reservationTimes.put(id, addReservationTime);
        return reservationTime;
    }

    @Override
    public void deleteById(Long id) {
        reservationTimes.remove(id);
    }
}
