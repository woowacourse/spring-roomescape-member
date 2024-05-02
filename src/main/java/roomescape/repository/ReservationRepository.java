package roomescape.repository;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    List<Reservation> findAllByDateAndTimeAndThemeId(LocalDate date, ReservationTime time, Long themeId);

    List<Reservation> findAll();

    boolean existById(Long id);

    void deleteById(Long id);

    int countByTimeId(Long timeId);

    List<Long> findAllTimeIdsByDateAndThemeId(LocalDate date, Long themeId);
}
