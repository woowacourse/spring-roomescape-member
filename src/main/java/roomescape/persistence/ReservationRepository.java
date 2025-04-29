package roomescape.persistence;

import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.business.Reservation;

public interface ReservationRepository extends GeneralRepository<Reservation> {

    boolean existsByDateTime(LocalDate date, LocalTime time);
}
