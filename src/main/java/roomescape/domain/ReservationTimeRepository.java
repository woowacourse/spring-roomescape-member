package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.domain.dto.AvailableTimeDto;

public interface ReservationTimeRepository {

    Optional<ReservationTime> findById(long id);

    ReservationTime create(ReservationTime reservationTime);

    List<ReservationTime> findAll();

    void deleteById(long id);

    boolean existsByStartAt(LocalTime time);

    List<AvailableTimeDto> findAvailableReservationTimes(LocalDate date, long themeId);
}
