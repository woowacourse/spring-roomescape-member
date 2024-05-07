package roomescape.domain.time.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import roomescape.domain.time.ReservationTime;

public interface ReservationTimeRepository {
    ReservationTime save(ReservationTime reservationTime);

    Optional<ReservationTime> findById(long id);

    List<ReservationTime> findAll();

    Set<Long> findReservedTimeIds(long themeId, LocalDate date);

    boolean deleteById(long id);
}
