package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import roomescape.model.Reservation;

public interface ReservationDao {
    Reservation save(Reservation reservation);

    boolean deleteById(Long id);

    List<Reservation> findAll();

    boolean isExistByTimeId(Long timeId);

    boolean isExistByThemeIdAndTimeIdAndDate(Long themeId, Long timeId, LocalDate date);

    boolean isExistByThemeId(Long themeId);
}
