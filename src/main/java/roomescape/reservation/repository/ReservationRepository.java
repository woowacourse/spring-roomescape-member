package roomescape.reservation.repository;

import roomescape.reservation.Reservation;
import roomescape.reservation.repository.dto.ReservationDetailFind;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<ReservationDetailFind> findAllDetails();

    void deleteById(long id);

    Set<Long> findTimeIdByDateAndThemeId(LocalDate date, long themeId);
}
