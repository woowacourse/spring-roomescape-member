package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.domain.ReservationTime;
import roomescape.dto.TimeWithBookStatusResponse;
import roomescape.repository.ReservationTimeRepository;

public class FakeReservationTimeRepository implements ReservationTimeRepository {

    Map<Long, ReservationTime> reservationTimes;
    AtomicLong atomicLong = new AtomicLong(0);

    FakeReservationTimeRepository() {
        this.reservationTimes = new HashMap<>();
    }

    FakeReservationTimeRepository(List<ReservationTime> reservationsTimes) {
        this.reservationTimes = new HashMap<>();
        for (int i = 0; i < reservationsTimes.size(); i++) {
            this.reservationTimes.put(reservationsTimes.get(i).getId(), reservationsTimes.get(i));
        }
    }

    @Override
    public List<ReservationTime> findAll() {
        return reservationTimes.values().stream().toList();
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        if (reservationTimes.containsKey(id)) {
            return Optional.of(reservationTimes.get(id));
        }
        return Optional.empty();
    }

    @Override
    public boolean existByStartAt(LocalTime startAt) {
        return reservationTimes.values().stream()
                .anyMatch((reservationTime -> reservationTime.getStartAt().equals(startAt)));
    }

    @Override
    public ReservationTime save(ReservationTime reservationTimeAddRequest) {
        Long id = atomicLong.incrementAndGet();
        ReservationTime reservationTime = new ReservationTime(id, reservationTimeAddRequest.getStartAt());
        reservationTimes.put(id, reservationTime);
        return reservationTime;
    }

    @Override
    public void deleteById(Long id) {
        reservationTimes.remove(id);
    }

    @Override
    public List<TimeWithBookStatusResponse> findAllWithBookStatus(LocalDate date, Long themeId) {
        return null;
    }
}
