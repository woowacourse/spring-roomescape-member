package roomescape.dao;

import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationRepository {
    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    void deleteById(long id);

    boolean existsByTimeId(long id);

    boolean existsByThemeId(long id);

    List<Reservation> findByThemeAndMemberAndDate(long themeId, long memberId, String dateFrom, String dateTo);
}
