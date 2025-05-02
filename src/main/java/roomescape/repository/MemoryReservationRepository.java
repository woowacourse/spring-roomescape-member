package roomescape.repository;

import java.util.List;
import roomescape.dto.ReservationValueDto;
import roomescape.model.Reservation;

public class MemoryReservationRepository implements ReservationRepository {

    @Override
    public List<Reservation> getAllReservations() {
        return List.of();
    }

    @Override
    public Reservation addReservation(ReservationValueDto reservationValueDto) {
        return null;
    }

    @Override
    public void deleteReservation(Long id) {

    }
}
