package roomescape.repository.reservation;

import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    boolean hasSameReservation(String date, Long timeId, Long themeId);

    void delete(Long id);

    List<Reservation> findAll();
}
