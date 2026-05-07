package roomescape.reservation.infra;

import roomescape.reservation.domain.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    void deleteById(Long id);

    List<Long> findTimeIdByDateAndThemeId(LocalDate date, long themeId);

    boolean existReservationByTimeId(long timeId);
}
