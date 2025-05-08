package roomescape.persistence.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.business.domain.Reservation;
import roomescape.presentation.dto.ReservationAvailableTimeResponse;

public interface ReservationDao {

    Long save(Reservation reservation);

    List<Reservation> findAll();

    boolean remove(Long id);

    boolean existsByDateAndTimeAndTheme(LocalDate date, Long timeId, Long themeId);

    List<ReservationAvailableTimeResponse> findAvailableTimesByDateAndTheme(LocalDate date, Long themeId);

    Optional<Reservation> findById(Long id);
}
