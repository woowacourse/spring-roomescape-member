package roomescape.repository;

import roomescape.domain.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {
    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    int deleteById(Long reservationId);

    boolean existByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId);

    boolean existByTimeId(Long reservationTimeId);

    boolean existByThemeId(Long themeId);

    List<Reservation> findAllByDateAndThemeId(LocalDate date, Long themeId);
}
