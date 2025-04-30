package roomescape.persistence;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import roomescape.business.Reservation;

public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation findById(Long id);

    Long add(Reservation reservation);

    void deleteById(Long id);

    boolean existsByDateTime(LocalDate date, LocalTime time);

    boolean existsByTimeId(Long timeId);
}
