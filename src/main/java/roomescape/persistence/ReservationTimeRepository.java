package roomescape.persistence;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.business.domain.reservation.ReservationTime;
import roomescape.presentation.member.dto.AvailableTimesResponseDto;

public interface ReservationTimeRepository {

    List<ReservationTime> findAll();

    Optional<ReservationTime> findById(Long id);

    ReservationTime add(ReservationTime reservationTime);

    void deleteById(Long id);

    List<AvailableTimesResponseDto> findAvailableTimes(LocalDate date, Long themeId);

    boolean existsByStartAt(LocalTime startAt);
}
