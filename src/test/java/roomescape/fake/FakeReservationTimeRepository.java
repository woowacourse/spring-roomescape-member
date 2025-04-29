package roomescape.fake;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

public final class FakeReservationTimeRepository implements ReservationTimeRepository {

    private static final List<ReservationTime> REPOSITORY = new ArrayList<>();
    private static final AtomicLong AUTO_INCREMENT = new AtomicLong(1);

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        ReservationTime saveTarget = new ReservationTime(AUTO_INCREMENT.getAndIncrement(),
                reservationTime.getStartAt());
        REPOSITORY.add(saveTarget);
        return saveTarget;
    }

    @Override
    public List<ReservationTime> getAll() {
        return new ArrayList<>(REPOSITORY);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return REPOSITORY.stream()
                .filter(reservationTime -> Objects.equals(id, reservationTime.getId()))
                .findFirst();
    }

    @Override
    public void remove(ReservationTime reservation) {
        REPOSITORY.remove(reservation);
    }

    public static void clear() {
        REPOSITORY.clear();
        AUTO_INCREMENT.set(1);
    }
}
