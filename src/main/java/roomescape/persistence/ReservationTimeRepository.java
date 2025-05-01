package roomescape.persistence;

import java.time.LocalDate;
import java.util.List;
import roomescape.business.ReservationTime;
import roomescape.business.dto.AvailableTimesResponseDto;

public interface ReservationTimeRepository {

    List<ReservationTime> findAll();

    ReservationTime findById(Long id);

    Long add(ReservationTime reservationTime);

    void deleteById(Long id);

    List<AvailableTimesResponseDto> findAvailableTimes(LocalDate date, Long themeId);
}
