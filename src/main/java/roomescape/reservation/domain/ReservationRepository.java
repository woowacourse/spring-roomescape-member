package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository {

    Long save(final Reservation reservation);

    int deleteById(final Long id);

    Reservation findById(final Long id);

    List<Reservation> findAll();

    boolean existByReservationTimeId(final Long timeId);

    boolean existByDateTime(final LocalDate date, final LocalTime time);

    boolean existByThemeId(final Long themeId);
}
