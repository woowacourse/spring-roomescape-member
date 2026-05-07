package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;

public interface ReservationRepository {
    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    List<Long> findTimeIdsByThemeIdAndDate(Long themeId, LocalDate date);

    boolean deleteById(Long id);

    boolean isDuplicated(Long themeId, ReservationTime time, LocalDate date);
}
