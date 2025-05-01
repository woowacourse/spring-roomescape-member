package roomescape.repository;

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
    public ReservationTime addTime(ReservationTime reservationTime) {
        ReservationTime reservationTimeWithId = new ReservationTime(id.getAndIncrement(), reservationTime.getStartAt());
        reservationTimes.add(reservationTimeWithId);
        return reservationTimeWithId;
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
    public ReservationTime findById(Long id) {
        for (ReservationTime reservationTime : reservationTimes) {
            if (reservationTime.getId().equals(id)) {
                return reservationTime;
            }
        }
        return null;
    }
}
