package roomescape.domain.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.reservation.entity.Reservation;

public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    void deleteById(Long id);

    boolean existsByThemeIdAndDateAndTimeId(Long themeId, LocalDate date, Long timeId);
}
