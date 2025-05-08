package roomescape.persistence.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.business.domain.Reservation;
import roomescape.presentation.dto.ReservationAvailableTimeResponse;

public interface ReservationDao {

    Long insert(Reservation reservation);

    List<Reservation> findAll();

    Optional<Reservation> findById(Long id);

    boolean deleteById(Long id);

    boolean existsByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId);

    List<ReservationAvailableTimeResponse> findAvailableTimesByDateAndThemeId(LocalDate date, Long themeId);
}
