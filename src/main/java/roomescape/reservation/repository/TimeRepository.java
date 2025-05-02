package roomescape.reservation.repository;

import roomescape.reservation.dto.response.AvailableTimeResponse;
import roomescape.reservation.entity.Time;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TimeRepository {

    Time save(Time time);

    List<Time> findAll();

    boolean deleteById(Long id);

    Optional<Time> findById(Long id);

    List<AvailableTimeResponse> findAvailableTimes(LocalDate date, Long themeId);
}
