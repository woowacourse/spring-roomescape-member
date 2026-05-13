package roomescape.reservation.repository;

import roomescape.reservation.Reservation;
import roomescape.reservation.repository.projection.ReservationDetailProjection;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<ReservationDetailProjection> findAllDetails();

    int deleteById(long id);

    Set<Long> findTimeIdByDateAndThemeId(LocalDate date, long themeId);

    List<ReservationDetailProjection> findDetailsByName(String name);

    int deleteByIdAndName(long id, String name);

    Optional<ReservationDetailProjection> findDetailByIdAndName(long reservationId, String name);

    boolean isDuplicateReservation(long reservationId, long scheduleId);

    int updateScheduleByIdAndName(long id, String name, long scheduleId);

    Optional<Reservation> findByIdAndName(long reservationId, String name);
}
