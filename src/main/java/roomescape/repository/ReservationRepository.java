package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationRepository {
    List<Reservation> read();
    Reservation findById(long id);
    void delete(long id);
    Reservation create(String name, LocalDate date, Long timeId);
}
