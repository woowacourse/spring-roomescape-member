package roomescape.fake;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import roomescape.domain.Reservation;
import roomescape.repository.ReservationRepository;

public class FakeReservationRepository implements ReservationRepository {

    // TODO: static 남겨둘지?
    private static final List<Reservation> REPOSITORY = new ArrayList<>();
    private static final AtomicLong AUTO_INCREMENT = new AtomicLong(1);

    @Override
    public Reservation save(Reservation reservation) {
        Reservation saveTarget = new Reservation(
                AUTO_INCREMENT.getAndIncrement(),
                reservation.name(),
                reservation.date(),
                reservation.time()
        );
        REPOSITORY.add(saveTarget);
        return saveTarget;
    }

    @Override
    public List<Reservation> getAll() {
        return new ArrayList<>(REPOSITORY);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return REPOSITORY.stream()
                .filter(reservation -> Objects.equals(id, reservation.id()))
                .findFirst();
    }

    @Override
    public void remove(Reservation reservation) {
        REPOSITORY.remove(reservation);
    }

    public static void clear() {
        REPOSITORY.clear();
        AUTO_INCREMENT.set(1);
    }
}
