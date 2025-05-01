package roomescape.fake;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationTimeRepository;

public final class FakeReservationTimeRepository implements ReservationTimeRepository {

    private final List<ReservationTime> repository = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public ReservationTime save(ReservationTime reservationTime) {
        ReservationTime saveTarget = new ReservationTime(idGenerator.getAndIncrement(),
                reservationTime.startAt());
        repository.add(saveTarget);
        return saveTarget;
    }

    @Override
    public List<ReservationTime> getAll() {
        return new ArrayList<>(repository);
    }

    @Override
    public Optional<ReservationTime> findById(Long id) {
        return repository.stream()
                .filter(reservationTime -> Objects.equals(id, reservationTime.id()))
                .findFirst();
    }

    @Override
    public void remove(ReservationTime reservation) {
        repository.remove(reservation);
    }

    @Override
    public List<ReservationTime> getAllByThemeIdAndDate(Long themeId, LocalDate date) {
        return List.of();
    }

    @Override
    public ReservationTime getById(Long id) {
        return null;
    }
}
