package roomescape.domain.repository;

import roomescape.domain.model.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    int deleteById(Long id);

    boolean existByTimeIdAndThemeIdAndDate(Long timeId, Long themeId, LocalDate date);

    List<Long> findBookedTimes(Long themeId, LocalDate date);

    boolean existByThemeId(Long themeId);

    boolean existByTimeId(Long timeId);
}
