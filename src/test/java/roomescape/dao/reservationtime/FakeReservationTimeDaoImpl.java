package roomescape.dao.reservationtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.reservationtime.ReservationTime;

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
        ReservationTime reservationTime = reservationTimes.stream()
                .filter(time -> time.getId().equals(id))
                .findFirst()
                .orElse(null);

        reservationTimes.remove(reservationTime);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return reservationTimes.stream()
                .filter(reservationTime -> reservationTime.getId().equals(id))
                .findFirst();
    }
}
