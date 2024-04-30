package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationRepository {

    Long save(Reservation reservation);

    List<Reservation> findAll();

    Reservation findById(Long id);

    void delete(Long id);

    Boolean existId(Long id);

    Boolean existTimeId(Long id);

    Boolean existDateAndTimeId(LocalDate date, Long timeId);
}
