package roomescape.business.model.repository;

import roomescape.business.model.entity.Reservation;
import roomescape.business.model.entity.Theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    List<Reservation> findAllWithFilter(final Long themeId, final Long memberId, final LocalDate dateFrom, final LocalDate dateTo);

    Optional<Reservation> findById(long id);

    boolean existById(long id);

    boolean existByTimeId(long timeId);

    boolean existByThemeId(long themeId);

    boolean isDuplicateDateAndTimeAndTheme(LocalDate date, LocalTime time, Theme theme);

    void deleteById(long id);
}
