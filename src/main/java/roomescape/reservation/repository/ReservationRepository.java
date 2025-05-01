package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation_time.domain.ReservationTime;
import roomescape.theme.domain.Theme;

public interface ReservationRepository {

    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    Reservation findByIdOrThrow(Long id);

    List<Reservation> findByThemeAndDate(Theme theme, LocalDate date);

    Reservation add(Reservation reservation);

    void delete(Long id);

    boolean existsByReservationTime(ReservationTime reservationTime);
}
