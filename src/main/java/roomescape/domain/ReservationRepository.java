package roomescape.domain;

import roomescape.persistence.query.CreateReservationQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface ReservationRepository {

    List<Reservation> findAll();

    Long create(CreateReservationQuery createReservationQuery);

    void deleteById(Long reservationId);

    Optional<Reservation> findById(Long reservationId);

    boolean existByTimeId(Long reservationTimeId);

    boolean existByDateAndTimeIdAndThemeId(LocalDate reservationDate, Long timeId, Long themeId);

    boolean existByThemeId(Long themeId);

    List<Reservation> findByThemeIdAndReservationDate(Long themeId, LocalDate reservationDate);
}
