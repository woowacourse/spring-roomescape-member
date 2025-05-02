package roomescape.repository;

import java.util.List;
import roomescape.dto.ReservationValueDto;
import roomescape.model.Reservation;

public interface ReservationRepository {

    List<Reservation> getAllReservations();

    Reservation addReservation(ReservationValueDto reservationValueDto);

    void deleteReservation(Long id);

}
