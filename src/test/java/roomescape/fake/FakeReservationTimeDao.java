package roomescape.fake;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.response.ReservationTimeWithIsBookedGetResponse;

public class FakeReservationTimeDao implements ReservationTimeDao {

    private final List<ReservationTime> times = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(1);

    public List<ReservationTime> findAll() {
        return new ArrayList<>(times);
    }

    public ReservationTime findById(Long id) {
        return times.stream()
            .filter(time -> time.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("예약 시간을 찾을 수 없습니다."));
    }

    @Override
    public List<ReservationTimeWithIsBookedGetResponse> findByDateAndThemeIdWithIsBookedOrderByStartAt(LocalDate date, Long themeId) {
        return List.of();
    }

    public ReservationTime add(ReservationTime reservationTime) {
        ReservationTime saved = new ReservationTime(
            index.getAndIncrement(),
            reservationTime.getStartAt()
        );
        times.add(saved);
        return saved;
    }

    public int deleteById(Long id) {
        times.removeIf(time -> time.getId().equals(id));
        return 1;
    }

    @Override
    public boolean existByStartAt(LocalTime startAt) {
        return times.stream()
            .anyMatch(time -> time.getStartAt().equals(startAt));
    }
}
