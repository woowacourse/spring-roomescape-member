package roomescape.service;

import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class ReservationTimeFakeRepository implements ReservationTimeRepository {

    List<ReservationTime> reservationTimes = new ArrayList<>(List.of(
            new ReservationTime(1L, LocalTime.of(10, 15)),
            new ReservationTime(2L, LocalTime.of(11, 20)),
            new ReservationTime(3L, LocalTime.of(12, 25))
    ));
    AtomicLong index = new AtomicLong(4);

    @Override
    public List<ReservationTime> findAll() {
        return List.copyOf(reservationTimes);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return reservationTimes.stream()
                .filter(time -> time.getId().equals(id))
                .findAny();
    }

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        ReservationTime assignedTime = reservationTime.assignId(index.getAndIncrement());
        reservationTimes.add(assignedTime);
        return assignedTime;
    }

    @Override
    public int deleteById(Long id) {
        if (reservationTimes.removeIf(time -> time.getId().equals(id))) {
            return 1;
        }
        return 0;
    }
}
