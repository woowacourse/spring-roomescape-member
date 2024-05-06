package roomescape.repository.mock;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import roomescape.domain.ReservationTime;
import roomescape.repository.TimeDao;

public class InMemoryTimeDao implements TimeDao {

    public List<ReservationTime> times;

    public InMemoryTimeDao() {
        this.times = new ArrayList<>();
    }

    @Override
    public List<ReservationTime> findAll() {
        return times;
    }

    @Override
    public ReservationTime findById(long id) {
        return times.stream()
                .filter(time -> time.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 예약 시간입니다."));
    }

    @Override
    public long save(ReservationTime reservationTime) {
        times.add(reservationTime);
        return times.size();
    }

    @Override
    public boolean existByTime(LocalTime time) {
        return times.stream()
                .anyMatch(target -> target.getStartAt() == time);
    }

    @Override
    public void deleteById(Long id) {
        ReservationTime reservationTime = times.stream()
                .filter(time -> time.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 예약 시간입니다."));

        times.remove(reservationTime);
    }
}
