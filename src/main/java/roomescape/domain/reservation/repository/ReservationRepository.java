package roomescape.domain.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.reservation.Reservation;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    Optional<Reservation> findById(long id);

    Optional<Reservation> findByTimeId(long timeId);

    boolean existsByReservationDateTimeAndTheme(LocalDate date, long timeId, long themeId);

    List<Reservation> findAll();

    void deleteById(long id);

    List<Reservation> findByThemeIdAndMemberIdAndDateFromAndDateTo(long themeId, long memberId, LocalDate dateTo,
                                                                   LocalDate dateFrom);
}
