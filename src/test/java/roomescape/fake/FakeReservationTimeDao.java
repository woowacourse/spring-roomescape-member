package roomescape.fake;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;

public class FakeReservationTimeDao implements ReservationTimeDao {

    private final List<ReservationTime> times = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(1);

    public List<ReservationTime> findAllTimes() {
        return new ArrayList<>(times);
    }

    public ReservationTime findTimeById(Long id) {
        return times.stream()
            .filter(time -> time.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("예약 시간을 찾을 수 없습니다."));
    }

    public ReservationTime addTime(ReservationTime reservationTime) {
        ReservationTime saved = new ReservationTime(
            index.getAndIncrement(),
            reservationTime.getStartAt()
        );
        times.add(saved);
        return saved;
    }

    public void removeTimeById(Long id) {
        times.removeIf(time -> time.getId().equals(id));
    }

    @Override
    public boolean existTimeByStartAt(LocalTime startAt) {
        return times.stream()
            .anyMatch(time -> time.getStartAt().equals(startAt));
    }
}
