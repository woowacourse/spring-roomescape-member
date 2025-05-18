package roomescape.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.entity.ReservationTime;
import roomescape.dto.response.AvailableReservationTimeResponse;

public interface ReservationTimeRepository {

    ReservationTime save(final ReservationTime reservationTime);

    List<ReservationTime> findAll();

    void deleteById(final Long id);

    List<AvailableReservationTimeResponse> findAllAvailable(final LocalDate date, final Long themeId);

    Optional<ReservationTime> findById(final Long id);
}
