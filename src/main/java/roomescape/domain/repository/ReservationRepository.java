package roomescape.domain.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationCondition;

public interface ReservationRepository {
    List<Reservation> findAll();

    List<Reservation> findByCondition(ReservationCondition cond);

    Reservation create(Reservation reservation);

    void delete(Long id);

    List<Reservation> findByTimeId(Long id);

    Optional<Reservation> findById(Long id);

    Optional<Reservation> findByDateTimeAndTheme(LocalDate date, LocalTime time, Theme theme);
}

