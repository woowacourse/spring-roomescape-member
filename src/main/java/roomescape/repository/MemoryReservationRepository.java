package roomescape.repository;

import java.util.List;
import roomescape.dto.ReservationValuesDto;
import roomescape.model.Reservation;

public class MemoryReservationRepository implements ReservationRepository {


    @Override
    public List<Reservation> getAllReservations() {
        return List.of();
    }

    @Override
    public Reservation addReservation(final ReservationValuesDto reservationValuesDto) {
        return null;
    }

    @Override
    public void deleteReservation(final Long id) {

    }
}
