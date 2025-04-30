package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.dto.ReservationRequestDto;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

public interface ReservationRepository {

    List<Reservation> getAllReservations();

    Reservation addReservation(ReservationRequestDto reservationRequestDto, ReservationTime reservationTime,
                               Theme theme);

    void deleteReservation(Long id);

    boolean contains(LocalDate reservationDate, Long timeId);
}
