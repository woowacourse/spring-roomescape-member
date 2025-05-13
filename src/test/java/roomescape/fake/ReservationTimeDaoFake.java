package roomescape.fake;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import roomescape.domain.model.ReservationTime;
import roomescape.infrastructure.dao.ReservationTimeDao;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class ReservationTimeDaoFake implements ReservationTimeDao {

    private static final AtomicLong IDX = new AtomicLong();
    private static final Map<Long, ReservationTime> TIMES = new HashMap<>();

    @Override
    public ReservationTime findById(Long id) {
        if (!TIMES.containsKey(id)) {
            throw new EmptyResultDataAccessException(0);
        }
        return TIMES.get(id);
    }

    @Override
    public ReservationTime save(ReservationTime time) {
        long count = TIMES.values().stream()
                .filter(savedTime -> savedTime.getStartAt().equals(time.getStartAt()))
                .count();

        if (count != 0) {
            throw new DuplicateKeyException("중복 시간 존재");
        }

        Long id = IDX.getAndIncrement();
        ReservationTime newTime = new ReservationTime(id, time.getStartAt());
        TIMES.put(id, newTime);
        return newTime;
    }

    @Override
    public List<ReservationTime> findAll() {
        return TIMES.values().stream().toList();
    }

    @Override
    public int deleteById(Long id) {
        if (TIMES.containsKey(id)) {
            TIMES.remove(id);
            return 1;
        }
        return 0;
    }

    @Override
    public boolean existByTimeValue(final LocalTime localTime) {
        long count = TIMES.values().stream()
                .filter(time -> time.getStartAt().equals(localTime))
                .count();
        return count != 0;
    }

    public void clear() {
        TIMES.clear();
        IDX.set(0);
    }
}
