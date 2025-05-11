package roomescape.persistence.dao;

import java.time.LocalDate;
import java.util.List;
import roomescape.business.domain.PlayTime;
import roomescape.business.domain.Reservation;
import roomescape.business.domain.Theme;
import roomescape.presentation.dto.reservation.ReservationAvailableTimeResponse;
import roomescape.presentation.dto.reservation.ReservationFilterDto;

public interface ReservationDao {

    Long save(Reservation reservation);

    List<Reservation> findAll();

    boolean remove(Long id);

    boolean existsByDateAndTimeAndTheme(LocalDate date, PlayTime time, Theme theme);

    List<ReservationAvailableTimeResponse> findAvailableTimesByDateAndTheme(LocalDate date, Theme theme);

    List<Reservation> findByFilter(ReservationFilterDto filter);
}
