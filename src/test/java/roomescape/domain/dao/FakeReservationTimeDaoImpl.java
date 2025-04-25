package roomescape.domain.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;

public class FakeReservationTimeDaoImpl implements ReservationTimeDao {

    private final AtomicLong index = new AtomicLong(1);
    private final List<ReservationTime> reservationTimes = new ArrayList<>();

    @Override
    public List<ReservationTime> findAllReservationTimes() {
        return Collections.unmodifiableList(reservationTimes);
    }

    @Override
    public void saveReservationTime(ReservationTime reservationTime) {
        reservationTime.setId(index.getAndIncrement());
        reservationTimes.add(reservationTime);
    }

    @Override
    public void deleteReservationTime(Long id) {
        ReservationTime reservationTime = findById(id);
        reservationTimes.remove(reservationTime);
    }

    @Override
    public ReservationTime findById(Long id) {
        return reservationTimes.stream()
            .filter(reservationTime -> reservationTime.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약번호 입니다."));
    }
}
