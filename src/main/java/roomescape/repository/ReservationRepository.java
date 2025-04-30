package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.dto.ReservationRequestDto;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;

public interface ReservationRepository {

    List<Reservation> getAllReservations();

    Reservation addReservation(ReservationRequestDto reservationRequestDto, ReservationTime reservationTime);

    void deleteReservation(Long id);

    boolean contains(LocalDate reservationDate, Long timeId);
}
