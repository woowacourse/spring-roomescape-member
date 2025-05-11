package roomescape.business.model.repository;

import roomescape.business.model.entity.Reservation;
import roomescape.business.model.entity.Theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    void save(Reservation reservation);

    List<Reservation> findAll();

    List<Reservation> findAllWithFilter(final String themeId, final String memberId, final LocalDate dateFrom, final LocalDate dateTo);

    Optional<Reservation> findById(String id);

    boolean existById(String id);

    boolean existByTimeId(String timeId);

    boolean existByThemeId(String themeId);

    boolean isDuplicateDateAndTimeAndTheme(LocalDate date, LocalTime time, Theme theme);

    void deleteById(String id);
}
