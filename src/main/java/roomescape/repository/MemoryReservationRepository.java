package roomescape.repository;

import java.util.List;

import roomescape.model.Reservation;

public class MemoryReservationRepository implements ReservationRepository {

    @Override
    public List<Reservation> getAllReservations() {
        return List.of();
    }

    @Override
    public Reservation addReservation(Reservation reservation) {
        return null;
    }

    @Override
    public void deleteReservation(Long id) {

    }
}
