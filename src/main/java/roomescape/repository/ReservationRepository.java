package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    void delete(long id);

    boolean existByDateAndTimeId(LocalDate date, long timeId);

    List<Reservation> findAll();
}
