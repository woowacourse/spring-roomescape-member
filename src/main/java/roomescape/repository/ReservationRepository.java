package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.cglib.core.Local;
import roomescape.controller.api.reservation.dto.ReservationSearchFilter;
import roomescape.model.Reservation;
import roomescape.model.Theme;

public interface ReservationRepository {

    boolean existsByDateTime(final LocalDate date, final LocalTime time);

    List<Reservation> findAll();

    List<Reservation> findAllByFilter(final ReservationSearchFilter filter);

    List<Reservation> findAllByTimeSlotId(final Long id);

    List<Reservation> findAllByThemeId(final Long id);

    List<Reservation> findAllByDateAndThemeId(final LocalDate date, final Long themeId);

    List<Theme> findPopularThemesByPeriod(final LocalDate startDate, final LocalDate endDate, final Integer limit);

    Long save(final Reservation reservation);

    Boolean removeById(final Long id);
}
