package roomescape.dao;

import java.util.List;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;

public interface ReservationDao {

    List<Reservation> findAllReservation();

    void saveReservation(Reservation reservation);

    void deleteReservation(Long id);

    boolean existsByTimeId(Long id);

    Boolean existsByDateAndTime(ReservationDate date, Long timeId);

    Boolean existReservationBy(String date, long timeId, Long themeId);
}
