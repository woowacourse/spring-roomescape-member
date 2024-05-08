package roomescape.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    Reservation save(final Reservation reservation);

    List<Reservation> findAll();

    Optional<Reservation> findById(final Long id);

    List<ReservationTime> findTimeByDateAndThemeId(final LocalDate date, final Long aLong);

    boolean existByTimeId(final Long timeId);

    boolean existByDateAndTimeIdAndThemeId(final LocalDate date, final Long timeId, final Long themeId);

    boolean existByThemeId(final Long themeId);

    void deleteById(final Long id);

    List<Theme> findTopThemesDurationOrderByCount(LocalDate startDate, LocalDate endDate, Integer limit);

    List<Reservation> findByDurationAndThemeIdAndMemberId(Long memberId, Long themeId, LocalDate dateFrom, LocalDate dateTo);
}
