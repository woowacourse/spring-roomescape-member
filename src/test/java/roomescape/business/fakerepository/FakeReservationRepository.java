package roomescape.business.fakerepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import roomescape.business.Reservation;
import roomescape.persistence.ReservationRepository;

public final class FakeReservationRepository implements ReservationRepository {

    private final List<Reservation> reservations = new ArrayList<>();

    @Override
    public List<Reservation> findAll() {
        return reservations;
    }

    @Override
    public Reservation findById(Long id) {
        return reservations.stream()
                .filter(reservation -> Objects.equals(reservation.getId(), id))
                .findFirst()
                .get();
    }

    @Override
    public Long add(Reservation reservation) {
        if (reservation.getId() == null) {
            reservation.setId(1L);
        }
        reservations.add(reservation);
        return reservation.getId();
    }

    @Override
    public void deleteById(Long id) {
        Reservation reservation = findById(id);
        reservations.remove(reservation);
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
