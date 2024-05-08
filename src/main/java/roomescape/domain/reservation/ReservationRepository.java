package roomescape.domain.reservation;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.reservation.Reservation;

public interface ReservationRepository {

    List<Reservation> findAll(Long memberId, Long themeId, LocalDate dateFrom, LocalDate dateTo);

    Reservation save(Reservation reservation);

    void deleteById(Long id);

    boolean existsById(Long id);

    boolean existsByTimeId(Long id);

    boolean existsByThemeId(Long id);

    boolean existsByReservation(LocalDate date, Long timeId, Long themeId);
}
