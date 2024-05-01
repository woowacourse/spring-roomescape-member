package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.model.Reservation;

public interface ReservationRepository {

    Reservation save(final Reservation reservation);

    List<Reservation> findAll();

    Optional<Reservation> findById(final Long id);

    List<Reservation> findByDateAndThemeId(final LocalDate date, final Long aLong);

    boolean existByTimeId(final Long timeId);

    boolean existByDateAndTimeIdAndThemeId(final LocalDate date, final Long timeId, final Long themeId);

    boolean existByThemeId(final Long themeId);

    void deleteById(final Long id);
}
