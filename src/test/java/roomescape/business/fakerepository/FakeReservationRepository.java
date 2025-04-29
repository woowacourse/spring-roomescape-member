package roomescape.business.fakerepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import roomescape.business.Reservation;
import roomescape.persistence.GeneralRepository;

public class FakeReservationRepository implements GeneralRepository<Reservation> {

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
    public void delete(Long id) {
        Reservation reservation = findById(id);
        reservations.remove(reservation);
    }
}
