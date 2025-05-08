package roomescape.domain.repository;

import roomescape.domain.model.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    int deleteById(Long id);

    boolean existByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId);

    List<Long> findBookedTimes(Long themeId, LocalDate date);

    boolean existByThemeId(Long themeId);

    boolean existByTimeId(Long timeId);
}
