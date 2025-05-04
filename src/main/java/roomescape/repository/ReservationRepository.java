package roomescape.repository;

import java.util.List;
import roomescape.dto.ReservationValuesDto;
import roomescape.model.Reservation;

public interface ReservationRepository {

    List<Reservation> getAllReservations();

    Reservation addReservation(ReservationValuesDto reservationValuesDto);

    void deleteReservation(Long id);

}
