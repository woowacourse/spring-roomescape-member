package roomescape.domain.reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    boolean existsByReservationDateTimeAndTheme(LocalDate date, long timeId, long themeId);

    boolean existsByTimeId(long timeId);

    boolean existsByThemeId(long themeId);

    List<Reservation> findAll();

    List<Reservation> findAllByThemeIdAndMemberIdAndDateRange(long themeId, long memberId, LocalDate dateFrom,
                                                              LocalDate dateTo);

    boolean deleteById(long id);
}
