package roomescape.service;

import roomescape.entity.ReservationTime;
import roomescape.exception.EntityNotFoundException;
import roomescape.repository.ReservationTimeDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class ReservationTimeInMemoryDao implements ReservationTimeDao {

    private List<ReservationTime> reservationTimes = new ArrayList<>();
    private AtomicLong atomic = new AtomicLong(1L);

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        ReservationTime generatedReservationTime = new ReservationTime(atomic.getAndIncrement(), reservationTime.getStartAt());
        reservationTimes.add(generatedReservationTime);
        return generatedReservationTime;
    }

    @Override
    public List<ReservationTime> findAll() {
        return reservationTimes;
    }

    @Override
    public void deleteById(Long id) {
        ReservationTime reservationTime = findById(id)
            .orElseThrow(() -> new EntityNotFoundException("삭제할 예약시간이 없습니다."));

        reservationTimes.remove(reservationTime);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return reservationTimes.stream()
            .filter(reservationTime -> reservationTime.isSameId(id))
            .findAny();
    }
}
