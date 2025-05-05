package roomescape.time.repository;

import roomescape.time.entity.ReservationTime;
import roomescape.time.repository.dto.ReservationTimeWithBookedDataResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {
    ReservationTime save(ReservationTime entity);

    List<ReservationTime> findAll();

    boolean deleteById(Long id);

    Optional<ReservationTime> findById(Long id);

    List<ReservationTimeWithBookedDataResponse> findAllWithBooked(LocalDate date, Long themeId);

    Optional<ReservationTime> findByStartAt(LocalTime startAt);
}
