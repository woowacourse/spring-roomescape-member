package roomescape.business.fakerepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import roomescape.business.Reservation;
import roomescape.persistence.ReservationRepository;

public final class FakeReservationRepository implements ReservationRepository {

    private final List<Reservation> reservations = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<Reservation> findAll() {
        return reservations;
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservations.stream()
                .filter(reservation -> Objects.equals(reservation.getId(), id))
                .findFirst();
    }

    @Override
    public Long add(Reservation reservation) {
        Reservation savedReservation = new Reservation(
                idGenerator.getAndIncrement(),
                reservation.getName(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme()
        );
        reservations.add(reservation);
        return savedReservation.getId();
    }

    @Override
    public void deleteById(Long id) {
        reservations.removeIf(reservation -> Objects.equals(reservation.getId(), id));
    }

    @Override
    public boolean existsByReservation(Reservation otherReservation) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.isSameReservation(otherReservation));
    }

    @Override
    public boolean existsByTimeId(Long timeId) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.getTime().getId().equals(timeId));
    }

    @Override
    public boolean existByThemeId(Long id) {
        return reservations.stream()
                .anyMatch(reservation -> reservation.getTheme().getId().equals(id));
    }
}
