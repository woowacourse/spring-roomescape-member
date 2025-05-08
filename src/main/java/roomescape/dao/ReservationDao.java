package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationDao {

    List<Reservation> findAllReservation();

    void saveReservation(Reservation reservation);

    void deleteReservation(Long id);

    Boolean existsReservationBy(LocalDate date, Long timeId, Long themeId);
}
