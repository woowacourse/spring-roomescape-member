package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;
import roomescape.dto.response.AvailableTimeResponse;

public interface ReservationTimeDao {
    List<ReservationTime> findAll();

    ReservationTime create(ReservationTime reservationTime);

    void delete(Long id);

    Optional<ReservationTime> findById(Long id);

    List<AvailableTimeResponse> findByDateAndThemeIdWithBooked(LocalDate date, Long themeId);
}
