package roomescape.fake;

import roomescape.global.exception.EntityNotFoundException;
import roomescape.time.entity.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ReservationTimeFakeRepository implements ReservationTimeRepository {

    private final Map<Long, ReservationTime> reservationTimes = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public ReservationTimeFakeRepository() {
        long reservationTimeId1 = idGenerator.getAndIncrement();
        ReservationTime defaultTime = new ReservationTime(reservationTimeId1, LocalTime.MAX);
        reservationTimes.put(reservationTimeId1, defaultTime);
        long reservationTimeId2 = idGenerator.getAndIncrement();
        ReservationTime defaultTime2 = new ReservationTime(reservationTimeId2, LocalTime.of(11, 0));
        reservationTimes.put(reservationTimeId2, defaultTime2);
    }

    @Override
    public ReservationTime findById(Long id) {
        ReservationTime reservationTime = reservationTimes.get(id);
        if (reservationTime == null) {
            throw new EntityNotFoundException("예약 시간을 찾을 수 없습니다: " + id);
        }
        return reservationTime;
    }

    @Override
    public List<ReservationTime> findAll() {
        return new ArrayList<>(reservationTimes.values());
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        long newId = idGenerator.getAndIncrement();

        ReservationTime savedTime = new ReservationTime(
                newId,
                reservationTime.getStartAt());

        reservationTimes.put(newId, savedTime);

        return savedTime;
    }

    @Override
    public void deleteById(Long id) {
        if (!reservationTimes.containsKey(id)) {
            throw new EntityNotFoundException("예약 시간을 찾을 수 없습니다: " + id);
        }

        reservationTimes.remove(id);
    }
}
