package roomescape.core.repository;

import java.util.List;
import roomescape.core.domain.Reservation;
import roomescape.core.dto.BookingTimeResponseDto;

public interface ReservationRepository {
    Long save(final Reservation reservation);

    List<Reservation> findAll();

    List<BookingTimeResponseDto> findAllByDateAndThemeId(final String date, final long themeId);

    Integer countByTimeId(final long timeId);

    Integer countByThemeId(final long themeId);

    Integer countByDateAndTimeIdAndThemeId(final String date, final long timeId, final long themeId);

    void deleteById(final long id);
}
