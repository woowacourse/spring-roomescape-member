package roomescape.repository;

import java.util.List;
import roomescape.domain.ReservationTime;
import roomescape.dto.response.ReservationTimeWithStateDto;

public interface ReservationTimeRepository {
    ReservationTime save(final ReservationTime reservationTime);

    List<ReservationTime> findAll();

    List<ReservationTimeWithStateDto> findAllWithReservationState(final String date, final long themeId);

    ReservationTime findById(final long id);

    boolean hasDuplicateReservationTime(final String startAt);

    void deleteById(final long id);
}
