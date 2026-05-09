package roomescape.service.support;

import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeReservationTimeRepository implements ReservationTimeRepository {

    private final List<ReservationTime> times = new ArrayList<>();
    private ReservationTime savedTime;
    private boolean deleteResult = true;

    @Override
    public List<ReservationTime> findAll() {
        return times;
    }

    @Override
    public Optional<ReservationTime> findById(final Long timeId) {
        return times.stream()
                .filter(time -> time.getId().equals(timeId))
                .findFirst();
    }

    @Override
    public ReservationTime save(final ReservationTime newReservationTime) {
        savedTime = newReservationTime;
        ReservationTime savedTimeWithId = ReservationTime.of(
                1L,
                newReservationTime.getStartAt(),
                newReservationTime.getEndAt()
        );
        times.add(savedTimeWithId);
        return savedTimeWithId;
    }

    @Override
    public boolean delete(final Long timeId) {
        return deleteResult;
    }

    public void add(final ReservationTime reservationTime) {
        times.add(reservationTime);
    }

    public ReservationTime savedTime() {
        return savedTime;
    }

    public void failToDelete() {
        deleteResult = false;
    }
}
