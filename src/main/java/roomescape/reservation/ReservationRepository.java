package roomescape.reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {

    Long save(Reservation reservation, Long reservationTimeId, Long themeId);

    Reservation findById(Long id);
    List<Reservation> findAll();
    List<Reservation> findAllByThemeIdAndDate(Long themeId, LocalDate date);

    void delete(Long id);

    Boolean existsById(Long id);
    Boolean existsByReservationTime(Long reservationTimeId);
    Boolean existsByTheme(Long themeId);
    Boolean existsByReservationTimeIdAndDate(Long reservationTimeId, LocalDate date);
}
