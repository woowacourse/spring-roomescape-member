package roomescape.domain.time;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ReservationTimeRepository {
    ReservationTime save(ReservationTime reservationTime);

    Optional<ReservationTime> findById(long id);

    List<ReservationTime> findAll();

    Set<Long> findReservedTimeIds(long themeId, LocalDate date);

    boolean deleteById(long id);
}
