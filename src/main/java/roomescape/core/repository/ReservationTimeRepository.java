package roomescape.core.repository;

import java.util.List;
import roomescape.core.domain.ReservationTime;
import roomescape.core.dto.BookingTimeResponseDto;

public interface ReservationTimeRepository {
    Long save(final ReservationTime reservationTime);

    List<ReservationTime> findAll();

    List<BookingTimeResponseDto> findAllByDateNotOrThemeIdNot(final String date, final long themeId);

    ReservationTime findById(final long id);

    boolean existByStartAt(final String startAt);

    void deleteById(final long id);
}
