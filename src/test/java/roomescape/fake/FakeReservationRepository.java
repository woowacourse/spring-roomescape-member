package roomescape.fake;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import roomescape.domain.Reservation;
import roomescape.repository.ReservationRepository;

public class FakeReservationRepository implements ReservationRepository {

    private final List<Reservation> repository = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Reservation save(Reservation reservation) {
        Reservation saveTarget = new Reservation(
                idGenerator.getAndIncrement(),
                reservation.name(),
                reservation.date(),
                reservation.time(),
                reservation.theme()
        );
        repository.add(saveTarget);
        return saveTarget;
    }

    @Override
    public List<Reservation> getAll() {
        return new ArrayList<>(repository);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return repository.stream()
                .filter(reservation -> Objects.equals(id, reservation.id()))
                .findFirst();
    }

    @Override
    public Reservation getById(Long id) {
        return null;
    }

    @Override
    public void remove(Reservation reservation) {
        repository.remove(reservation);
    }

    @Override
    public boolean existDuplicatedDateTime(LocalDate date, Long timeId, Long themeId) {
        return false;
    }
}
