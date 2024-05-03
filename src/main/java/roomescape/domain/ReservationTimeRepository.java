package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.domain.exception.EntityNotFoundException;

public interface ReservationTimeRepository {

    Optional<ReservationTime> findById(long id);

    ReservationTime getById(long id) throws EntityNotFoundException;

    ReservationTime create(ReservationTime reservationTime);

    List<ReservationTime> findAll();

    void deleteById(long id);

    boolean existsByStartAt(LocalTime time);

    List<TimeSlot> getReservationTimeAvailabilities(LocalDate date, long themeId);
}
