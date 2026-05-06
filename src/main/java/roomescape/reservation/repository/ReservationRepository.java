package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.entity.Reservation;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.theme.entity.Theme;

public interface ReservationRepository {

    Reservation save(String name, LocalDate date, ReservationTime reservationTime, Theme theme);

    Optional<Reservation> findById(Long id);

    List<Reservation> findAll();

    List<Long> findReservedTimeIdsByDateAndThemeId(LocalDate date, Long themeId);

    void deleteById(Long id);

}
