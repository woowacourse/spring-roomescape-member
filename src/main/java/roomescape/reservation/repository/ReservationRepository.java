package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation findByIdOrThrow(Long id);

    List<Reservation> findByThemeAndDate(Theme theme, LocalDate date);

    Reservation add(Reservation reservation);

    void delete(Long id);

    boolean existsByReservationTime(ReservationTime reservationTime);
}
