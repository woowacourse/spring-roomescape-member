package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;

import roomescape.dto.ReservationTimeBookedResponse;
import roomescape.model.ReservationTime;

import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {

    ReservationTime save(final ReservationTime reservationTime);

    Optional<ReservationTime> findById(final Long id);

    List<ReservationTime> findAll();

    void deleteById(final Long id);

    boolean existByStartAt(final LocalTime startAt);

    List<ReservationTimeBookedResponse> findTimesWithBooked(final LocalDate date, final Long themeId);
}
