package roomescape.repository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.model.ReservationTime;

public class MemoryReservationTimeRepository implements ReservationTimeRepository {
    private final List<ReservationTime> reservationTimes;
    private final AtomicLong id = new AtomicLong(1L);

    public MemoryReservationTimeRepository() {
        reservationTimes = new ArrayList<ReservationTime>();
    }

    @Override
    public ReservationTime addTime(LocalTime startAt) {
        ReservationTime reservationTime = new ReservationTime(id.getAndIncrement(), startAt);
        reservationTimes.add(reservationTime);
        return reservationTime;
    }

    @Override
    public List<ReservationTime> getAllTime() {
        return new ArrayList<>(reservationTimes);
    }

    @Override
    public void deleteTime(Long id) {
        for (ReservationTime reservationTime : reservationTimes) {
            if (reservationTime.getId().equals(id)) {
                reservationTimes.remove(reservationTime);
            }
        }
    }

    @Override
    public ReservationTime getReservationTimeById(Long id) {
        for (ReservationTime reservationTime : reservationTimes) {
            if (reservationTime.getId().equals(id)) {
                return reservationTime;
            }
        }
        return null;
    }
}
