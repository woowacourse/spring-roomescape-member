package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.dto.response.AvailableReservationTimeResponse;
import roomescape.reservation.entity.ReservationTime;

public interface ReservationTimeRepository {

    ReservationTime save(ReservationTime time);

    List<ReservationTime> findAll();

    boolean deleteById(Long id);

    Optional<ReservationTime> findById(Long id);

    List<AvailableReservationTimeResponse> findAvailableTimes(LocalDate date, Long themeId);
}
