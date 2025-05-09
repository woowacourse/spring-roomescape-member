package roomescape.reservation.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.entity.ReservationTime;

public interface ReservationTimeRepository {

    ReservationTime save(ReservationTime reservationTime);

    List<ReservationTime> getAll();

    Optional<ReservationTime> findById(Long id);

    ReservationTime getById(Long id);

    void remove(ReservationTime reservation);

    List<ReservationTime> getAllByThemeIdAndDate(Long themeId, LocalDate date);
}
