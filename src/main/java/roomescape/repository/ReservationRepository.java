package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.model.Reservation;

public interface ReservationRepository {

    Reservation save(final Reservation reservation);

    List<Reservation> findAll();

    void deleteById(final Long id);

    boolean existByTimeId(final Long timeId);

    Optional<Reservation> findById(final Long id);

    boolean existByDateAndTimeId(LocalDate date, Long timeId);

    boolean existByThemeId(Long themeId);
}
