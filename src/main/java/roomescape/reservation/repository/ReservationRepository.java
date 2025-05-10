package roomescape.reservation.repository;

import roomescape.reservation.entity.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    void deleteById(long id);

    List<Long> findBookedTimeIdsByDateAndThemeId(LocalDate date, Long themeId);
}
