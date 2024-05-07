package roomescape.core.repository;

import java.util.List;
import roomescape.core.domain.Reservation;
import roomescape.core.dto.BookingTimeResponseDto;

public interface ReservationRepository {
    Long save(final Reservation reservation);

    List<Reservation> findAll();

    List<BookingTimeResponseDto> findAllByDateNotOrThemeIdNot(final String date, final long themeId);

    List<Reservation> findAllByDateAndThemeId(final String date, final long themeId);

    boolean existByTimeId(final long timeId);

    boolean existByThemeId(final long themeId);

    boolean existByDateAndTimeIdAndThemeId(final String date, final long timeId, final long themeId);

    void deleteById(final long id);
}
