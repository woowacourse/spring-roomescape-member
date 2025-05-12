package roomescape.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.business.ReservationTime;
import roomescape.presentation.dto.response.AvailableTimesResponseDto;

public interface ReservationTimeRepository {

    List<ReservationTime> findAll();

    Optional<ReservationTime> findById(Long id);

    Long add(ReservationTime reservationTime);

    void deleteById(Long id);

    List<AvailableTimesResponseDto> findAvailableTimes(LocalDate date, Long themeId);
}
