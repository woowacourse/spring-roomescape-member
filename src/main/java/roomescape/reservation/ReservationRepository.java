package roomescape.reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {

    Long save(Reservation reservation, Long reservationTimeId, Long themeId);

    List<Reservation> findAll();
    Reservation findById(Long id);

    void delete(Long id);

    Boolean existsById(Long id);
    Boolean existsByReservationTimeIdAndDate(Long reservationTime, LocalDate date);
}
