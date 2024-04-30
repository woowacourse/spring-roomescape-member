package roomescape.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequest;

public class CollectionReservationTimeRepository implements ReservationTimeRepository {
    private final List<ReservationTime> reservationTimes;
    private final AtomicLong atomicLong;

    public CollectionReservationTimeRepository() {
        this(new ArrayList<>());
    }

    public CollectionReservationTimeRepository(List<ReservationTime> reservationTimes) {
        this(reservationTimes, new AtomicLong(0));
    }

    public CollectionReservationTimeRepository(List<ReservationTime> reservationTimes, AtomicLong atomicLong) {
        this.reservationTimes = reservationTimes;
        this.atomicLong = atomicLong;
    }

    @Override
    public ReservationTime save(ReservationTimeRequest reservationTimeRequest) {
        ReservationTime reservationTime = new ReservationTime(atomicLong.incrementAndGet(),
                reservationTimeRequest.startAt());
        reservationTimes.add(reservationTime);
        return reservationTime;
    }

    @Override
    public List<ReservationTime> findAll() {
        return List.copyOf(reservationTimes);
    }

    @Override
    public void delete(long id) {
        reservationTimes.stream()
                .filter(reservationTime -> reservationTime.getId().equals(id))
                .findAny()
                .ifPresent(reservationTimes::remove);
    }
}
